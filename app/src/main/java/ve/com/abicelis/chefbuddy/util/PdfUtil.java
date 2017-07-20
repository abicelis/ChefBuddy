package ve.com.abicelis.chefbuddy.util;

import android.annotation.TargetApi;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import java.util.Locale;

import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.app.ChefBuddyApplication;
import ve.com.abicelis.chefbuddy.model.Recipe;

/**
 * Created by abicelis on 19/7/2017.
 */

public class PdfUtil {

    private static final int PDF_HEIGHT = 1000;
    private static final int PDF_WIDTH = 600;
    private static final int FEATURED_IMAGE_HEIGHT = 200;
    private static final int HEADER_HEIGHT = 150;
    private static final int HEADER_TEXT_TITLE_SUBTITLE_SPACING = 15;

    private static final int HEADER_TOP = FEATURED_IMAGE_HEIGHT;
    private static final int CONTENT_TOP = HEADER_TOP + HEADER_HEIGHT;


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static PdfDocument generatePdfFromRecipe(Recipe recipe) {

        Paint primaryPaint = new Paint();
        primaryPaint.setColor(ContextCompat.getColor(ChefBuddyApplication.getContext(), R.color.primary));

        Paint whiteTextPaintLarge = new Paint();
        whiteTextPaintLarge.setColor(ContextCompat.getColor(ChefBuddyApplication.getContext(), R.color.white));
        whiteTextPaintLarge.setTextSize(50);
        whiteTextPaintLarge.setTextAlign(Paint.Align.CENTER);

        Paint whiteTextPaint = new Paint();
        whiteTextPaint.setColor(ContextCompat.getColor(ChefBuddyApplication.getContext(), R.color.white));
        whiteTextPaint.setTextSize(20);
        whiteTextPaint.setTextAlign(Paint.Align.CENTER);

        Paint primaryTextPaint = new Paint();
        primaryTextPaint.setColor(ContextCompat.getColor(ChefBuddyApplication.getContext(), R.color.primary_text));
        Paint secondaryTextPaint = new Paint();
        secondaryTextPaint.setColor(ContextCompat.getColor(ChefBuddyApplication.getContext(), R.color.secondary_text));



        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(PDF_WIDTH, PDF_HEIGHT, 1).create();

        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas pageCanvas = page.getCanvas();

        pageCanvas.drawBitmap(recipe.getFeaturedImage(), null, new Rect(0, 0, PDF_WIDTH, FEATURED_IMAGE_HEIGHT), null);
        pageCanvas.drawRect(0, FEATURED_IMAGE_HEIGHT, PDF_WIDTH, HEADER_TOP+HEADER_HEIGHT, primaryPaint);


        int xPos = (PDF_WIDTH / 2);
        int yPos = (int) (HEADER_TOP + (HEADER_HEIGHT / 2) - ((whiteTextPaint.descent() + whiteTextPaint.ascent()) / 2)) ;
        //((textPaint.descent() + textPaint.ascent()) / 2) is the distance from the baseline to the center.

        pageCanvas.drawText(recipe.getName(), xPos, yPos - HEADER_TEXT_TITLE_SUBTITLE_SPACING, whiteTextPaintLarge);
        pageCanvas.drawText(String.format(Locale.getDefault(),
                ChefBuddyApplication.getContext().getResources().getString(R.string.activity_recipe_detail_title_detail_format),
                recipe.getServings().getFriendlyName(),
                recipe.getPreparationTime().getFriendlyName()), xPos, yPos + HEADER_TEXT_TITLE_SUBTITLE_SPACING, whiteTextPaint);


        document.finishPage(page);



        return document;
    }
}
