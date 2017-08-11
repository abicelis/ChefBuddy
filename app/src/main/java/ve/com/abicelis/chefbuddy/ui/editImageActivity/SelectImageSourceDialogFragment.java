package ve.com.abicelis.chefbuddy.ui.editImageActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import ve.com.abicelis.chefbuddy.R;
import ve.com.abicelis.chefbuddy.model.ImageSourceType;


/**
 * Created by abice on 16/3/2017.
 */

public class SelectImageSourceDialogFragment extends DialogFragment implements View.OnClickListener {

    //DATA
    private ImageSourceSelectedListener mListener;

    //UI
    @BindView(R.id.dialog_select_image_source_camera)
    FloatingActionButton mCamera;

    @BindView(R.id.dialog_select_image_source_gallery)
    FloatingActionButton mGallery;

    @BindView(R.id.dialog_select_image_source_cancel)
    Button mCancel;


    public SelectImageSourceDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static SelectImageSourceDialogFragment newInstance() {
        SelectImageSourceDialogFragment frag = new SelectImageSourceDialogFragment();
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View dialogView =  inflater.inflate(R.layout.dialog_select_image_source, container);
        ButterKnife.bind(this, dialogView);


        mCamera.setOnClickListener(this);
        mGallery.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        return dialogView;
    }

    @Override
    public void show(FragmentManager manager, String tag) {

        //Workaround for fixing "IllegalStateException: Can not perform this action after onSaveInstanceState"
        //when executing dialog.show();
        //Fix is running same code as super, only replacing commit() with commitAllowingStateLoss()
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        mListener.onSourceSelected(ImageSourceType.NONE);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id) {
            case R.id.dialog_select_image_source_camera:
                mListener.onSourceSelected(ImageSourceType.CAMERA);
                break;

            case R.id.dialog_select_image_source_gallery:
                mListener.onSourceSelected(ImageSourceType.GALLERY);
                break;

            case R.id.dialog_select_image_source_cancel:
                mListener.onSourceSelected(ImageSourceType.NONE);
                break;
        }
        dismiss();
    }


    public void setListener(ImageSourceSelectedListener listener) {
        mListener = listener;
    }


    public interface ImageSourceSelectedListener {
        void onSourceSelected(ImageSourceType imageSourceType);
    }
}
