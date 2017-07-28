package ve.com.abicelis.chefbuddy.util;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import java.io.File;
import java.util.Locale;

import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.app.ChefBuddyApplication;
import ve.com.abicelis.chefbuddy.model.Recipe;
import ve.com.abicelis.chefbuddy.model.RecipeIngredient;

/**
 * Created by abicelis on 19/7/2017.
 */

public class PdfUtil {

    private static final float PDF_WIDTH = 600;
    private static final float PDF_CENTER = PDF_WIDTH/2;
    private static final float FEATURED_IMAGE_HEIGHT = 300;
    private static final float NORMAL_IMAGE_HEIGHT = 300;
    private static final float HEADER_HEIGHT = 150;
    private static final float SUBTITLE_HEIGHT = 60;
    private static final float INGREDIENT_HEIGHT = 50;


    private static final float HEADER_TEXT_TITLE_SUBTITLE_SPACING = 30;
    private static final float SUBTITLE_MARGIN_LEFT = 40;
    private static final float SUBTITLE_MARGIN_TOP = 40;
    private static final float INGREDIENT_MARGIN_LEFT = 80;
    private static final float PREPARATION_PARAGRAPH_MARGIN = 80;
    private static final float PREPARATION_PARAGRAPH_WIDTH = PDF_WIDTH - (2* PREPARATION_PARAGRAPH_MARGIN);
    private static final float NORMAL_IMAGE_MARGIN_HORIZONTAL = 20;
    private static final float NORMAL_IMAGE_MARGIN_VERTICAL = 40;

    private static final float FEATURED_IMAGE_ASPECT_RATIO = PDF_WIDTH/FEATURED_IMAGE_HEIGHT;
    private static final float NORMAL_IMAGE_ASPECT_RATIO = (PDF_WIDTH - (2*NORMAL_IMAGE_MARGIN_HORIZONTAL)) / NORMAL_IMAGE_HEIGHT;

    private static final float HEADER_TOP = FEATURED_IMAGE_HEIGHT;
    private static final float CONTENT_TOP = HEADER_TOP + HEADER_HEIGHT;




    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static PdfDocument generatePdfFromRecipe(Recipe recipe) {

        //Generate preparation text
        StaticLayout preparationTextLayout = getPreparationStaticLayout(recipe.getDirections(), PREPARATION_PARAGRAPH_WIDTH);
        float PREPARATION_PARAGRAPH_HEIGHT = preparationTextLayout.getHeight();

        //Height precalculation
        float PDF_HEIGHT = FEATURED_IMAGE_HEIGHT
                + HEADER_HEIGHT
                + SUBTITLE_HEIGHT * 3
                + SUBTITLE_MARGIN_TOP * 3
                + INGREDIENT_HEIGHT * recipe.getRecipeIngredients().size()
                + PREPARATION_PARAGRAPH_HEIGHT
                + NORMAL_IMAGE_MARGIN_VERTICAL * recipe.getImages().size()
                + NORMAL_IMAGE_HEIGHT * recipe.getImages().size()
                + NORMAL_IMAGE_MARGIN_VERTICAL;

        //Paints
        Paint primaryPaint = new Paint();
        primaryPaint.setColor(ContextCompat.getColor(ChefBuddyApplication.getContext(), R.color.primary));
        primaryPaint.setTextSize(32);

        Paint whiteTextPaintLarge = new Paint();
        whiteTextPaintLarge.setColor(ContextCompat.getColor(ChefBuddyApplication.getContext(), R.color.white));
        whiteTextPaintLarge.setTextSize(40);
        whiteTextPaintLarge.setTextAlign(Paint.Align.CENTER);

        Paint whiteTextPaint = new Paint();
        whiteTextPaint.setColor(ContextCompat.getColor(ChefBuddyApplication.getContext(), R.color.white));
        whiteTextPaint.setTextSize(26);
        whiteTextPaint.setTextAlign(Paint.Align.CENTER);

        Paint primaryTextPaint = new Paint();
        primaryTextPaint.setColor(ContextCompat.getColor(ChefBuddyApplication.getContext(), R.color.primary_text));
        primaryTextPaint.setTextSize(28);

        Paint secondaryTextPaint = new Paint();
        secondaryTextPaint.setColor(ContextCompat.getColor(ChefBuddyApplication.getContext(), R.color.secondary_text));
        secondaryTextPaint.setTextSize(20);

        Paint backgroundPaint = new Paint();
        backgroundPaint.setColor(ContextCompat.getColor(ChefBuddyApplication.getContext(), R.color.gray_background));


        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder((int)PDF_WIDTH, (int)PDF_HEIGHT, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas pageCanvas = page.getCanvas();


        //Featured image
        Bitmap featuredImage = getBitmapFromImageFile(recipe.getFeaturedImage());
        if(featuredImage != null) {
            Rect destRect = getDestRectForAspectRatio(featuredImage, FEATURED_IMAGE_ASPECT_RATIO);
            featuredImage = ThumbnailUtils.extractThumbnail(featuredImage, destRect.width(), destRect.height());
            pageCanvas.drawBitmap(featuredImage, null, new Rect(0, 0, (int) PDF_WIDTH, (int) FEATURED_IMAGE_HEIGHT), null);
        }
        // TODO: 28/7/2017 ELSE LOAD ""NO IMAGE"" DRAWABLE OR SOMETHING

        //Header background, title and subtitle
        pageCanvas.drawRect(0, FEATURED_IMAGE_HEIGHT, PDF_WIDTH, HEADER_TOP+HEADER_HEIGHT, primaryPaint);
        float yPos = HEADER_TOP + (HEADER_HEIGHT / 2);
        drawText(pageCanvas, yPos-HEADER_TEXT_TITLE_SUBTITLE_SPACING, PDF_CENTER, recipe.getName(), whiteTextPaintLarge);
        drawText(pageCanvas,
                yPos+HEADER_TEXT_TITLE_SUBTITLE_SPACING,
                PDF_CENTER,
                String.format(Locale.getDefault(),
                        ChefBuddyApplication.getContext().getResources().getString(R.string.activity_recipe_detail_title_detail_format),
                        recipe.getServings().getFriendlyName(),
                        recipe.getPreparationTime().getFriendlyName()),
                whiteTextPaint);


        //Content background
        pageCanvas.drawRect(0, CONTENT_TOP, PDF_WIDTH, PDF_HEIGHT, backgroundPaint);


        //Content
        float cursorY = CONTENT_TOP;



        //Ingredients subtitle
        drawIndicatorLine(pageCanvas, cursorY, Color.RED);
        cursorY +=SUBTITLE_MARGIN_TOP;
        drawIndicatorLine(pageCanvas, cursorY, Color.RED);
        drawText(pageCanvas, cursorY+(SUBTITLE_HEIGHT/2), SUBTITLE_MARGIN_LEFT, "INGREDIENTS", primaryPaint);
        cursorY +=SUBTITLE_HEIGHT;
        drawIndicatorLine(pageCanvas, cursorY, Color.RED);

        //Ingredients
        for (RecipeIngredient i : recipe.getRecipeIngredients()) {
            drawIndicatorLine(pageCanvas, cursorY, Color.RED);
            drawText(pageCanvas, cursorY + (INGREDIENT_HEIGHT/2), INGREDIENT_MARGIN_LEFT, "• " + i.getAmountString() + " " + i.getIngredient().getName(), secondaryTextPaint );
            cursorY +=INGREDIENT_HEIGHT;
        }


        //Preparation subtitle
        drawIndicatorLine(pageCanvas, cursorY, Color.RED);
        cursorY +=SUBTITLE_MARGIN_TOP;
        drawIndicatorLine(pageCanvas, cursorY, Color.RED);
        drawText(pageCanvas, cursorY+(SUBTITLE_HEIGHT/2), SUBTITLE_MARGIN_LEFT, "PREPARATION", primaryPaint);
        cursorY +=SUBTITLE_HEIGHT;
        drawIndicatorLine(pageCanvas, cursorY, Color.RED);


        //Preparation paragraph
        pageCanvas.save();                                  //Save the current matrix and clip
        pageCanvas.translate(PREPARATION_PARAGRAPH_MARGIN, cursorY);    //Apply translation to Canvas matrix to text's top left corner
        preparationTextLayout.draw(pageCanvas);             //Draw StaticLayout on Canvas
        pageCanvas.restore();                               //Revert matrix translation
        cursorY += PREPARATION_PARAGRAPH_HEIGHT;


        //Images subtitle
        drawIndicatorLine(pageCanvas, cursorY, Color.RED);
        cursorY +=SUBTITLE_MARGIN_TOP;
        drawIndicatorLine(pageCanvas, cursorY, Color.RED);
        drawText(pageCanvas, cursorY+(SUBTITLE_HEIGHT/2), SUBTITLE_MARGIN_LEFT, "IMAGES", primaryPaint);
        cursorY +=SUBTITLE_HEIGHT;
        drawIndicatorLine(pageCanvas, cursorY, Color.RED);


        for (String imageFilename : recipe.getImages()) {
            cursorY+=NORMAL_IMAGE_MARGIN_VERTICAL;
            Bitmap image = getBitmapFromImageFile(imageFilename);
            if(image == null)
                continue;

            Rect dest = getDestRectForAspectRatio(image, NORMAL_IMAGE_ASPECT_RATIO);

            while(true) {
                if(dest.width() > PDF_WIDTH || dest.height() > NORMAL_IMAGE_HEIGHT){
                    dest.set(0, 0, (int)((float)dest.right*0.9), (int)((float)dest.bottom*0.9));
                } else break;
            }

            image = ThumbnailUtils.extractThumbnail(image, dest.width(), dest.height());
            pageCanvas.drawBitmap(image,
                    null,
                    new Rect((int)NORMAL_IMAGE_MARGIN_HORIZONTAL,
                            (int)cursorY,
                            (int)(PDF_WIDTH-NORMAL_IMAGE_MARGIN_HORIZONTAL),
                            (int)(cursorY+NORMAL_IMAGE_HEIGHT)),
                    null);
            cursorY+=NORMAL_IMAGE_HEIGHT;
        }

        document.finishPage(page);
        return document;
    }


    private static Bitmap getBitmapFromImageFile(String imageFileName) {
        if(imageFileName == null) //Load default image
            return ImageUtil.getBitmap(R.drawable.default_recipe_image);
        else {
            try {
                return ImageUtil.getBitmap(new File(FileUtil.getImageFilesDir(), imageFileName));
            } catch (Exception e) {
                return ImageUtil.getBitmap(R.drawable.default_recipe_image);
            }
        }
    }

    private static void drawText(Canvas canvas, float posY, float posX, String text, Paint paint) {
        drawIndicatorLine(canvas, posY, Color.GREEN);
        posY += calculateHalfTextHeight(text, paint);
        canvas.drawText(text, posX, posY, paint);
    }

    //https://www.skoumal.net/en/android-drawing-multiline-text-on-bitmap/
    private static StaticLayout getPreparationStaticLayout(String text, float textWidth) {
        TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(ContextCompat.getColor(ChefBuddyApplication.getContext(), R.color.secondary_text));
        paint.setTextSize(20);

        return new StaticLayout(text, paint, (int)textWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
    }

    private static Rect getDestRectForAspectRatio(Bitmap bitmap, float aspectRatio) {
        int destWidth, destHeight;

        if(aspectRatio == 1) {
            int destSquareSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
            return new Rect(0,0,destSquareSize, destSquareSize);
        } else if(aspectRatio < 1) {
            destHeight = bitmap.getHeight();
            destWidth = (int)(((float)bitmap.getWidth())*aspectRatio);
        } else {
            destWidth = bitmap.getWidth();
            destHeight = (int)(((float)bitmap.getHeight())/aspectRatio);
        }
        return new Rect(0 ,0, destWidth, destHeight);
    }

    private static void drawIndicatorLine(Canvas canvas, float posY, int color) {
//        if(color == -1)
//            color = Color.RED;
//
//        Paint redPaint = new Paint();
//        redPaint.setColor(color);
//        canvas.drawLine(0, posY, canvas.getWidth(), posY, redPaint);
    }

    private static float calculateHalfTextHeight(String text, Paint paint) {
        //Calculate text bounds, grab the height, shift text pos by half the text height
        Rect r = new Rect();
        paint.getTextBounds(text, 0, text.length(), r);
        return (Math.abs(r.height()))/2;
    }
}
