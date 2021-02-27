package com.github.phuctranba.core.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.phuctranba.core.item.EnumLevelOfDifficult;
import com.github.phuctranba.core.item.EnumRecipeType;
import com.github.phuctranba.core.item.EnumStorage;
import com.github.phuctranba.core.util.ImageUtil;
import com.github.phuctranba.sharedkitchen.CreateRecipeActivity;
import com.github.phuctranba.sharedkitchen.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateGeneralInformationFragment extends Fragment {

    static Spinner spnLevelOfDifficult, spnType;
    static ImageView imageView;
    ImageView videoView;
    static EditText editTextName, editTextRequire;
    static RadioButton radioButtonLocal;
    private final int PICK_IMAGE_REQUEST = 1;
    static private Uri filePath;
    static String videoUrl;
    static boolean updateImage = false, updateVideo = false;
    static Bitmap bitmap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_create_general, container, false);
        setHasOptionsMenu(true);

        Init(rootView);

        setClick();

        return rootView;
    }

    void Init(View rootView) {
        spnLevelOfDifficult = rootView.findViewById(R.id.spnLevelOfDifficult);
        spnLevelOfDifficult.setAdapter(new ArrayAdapter<EnumLevelOfDifficult>(getActivity(), android.R.layout.simple_spinner_dropdown_item, EnumLevelOfDifficult.values()));

        spnType = rootView.findViewById(R.id.spnType);
        spnType.setAdapter(new ArrayAdapter<EnumRecipeType>(getActivity(), android.R.layout.simple_spinner_dropdown_item, EnumRecipeType.values()));

        imageView = rootView.findViewById(R.id.add_image);
        videoView = rootView.findViewById(R.id.add_video);
        editTextName = rootView.findViewById(R.id.name);
        editTextRequire = rootView.findViewById(R.id.require);
        radioButtonLocal = rootView.findViewById(R.id.rdo_local);
    }

    void setClick() {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAddVideoDialog();
            }
        });

    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void displayAddVideoDialog() {
        final LayoutInflater inflater = getLayoutInflater();
        final View dialogLayout = inflater.inflate(R.layout.dialog_create_step, null);
        final EditText editTextStep = (EditText) dialogLayout.findViewById(R.id.step);
        final TextView textViewTitle = dialogLayout.findViewById(R.id.title);
        textViewTitle.setText(R.string.add_video);
        editTextStep.setHint(R.string.add_video_hint);

        final AlertDialog.Builder editDialog = new AlertDialog.Builder(getActivity());
        editDialog.setView(dialogLayout);
        editDialog.setCancelable(false);
        editDialog.setNegativeButton("Hủy", null);

        editDialog.setPositiveButton("Lưu", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                videoUrl = editTextStep.getText().toString().trim();

                if (isYoutubeUrl(videoUrl)) {
                    Glide
                            .with(getActivity())
                            .load(getYoutubeVideoIdFromUrl(videoUrl))
                            .centerCrop()
                            .placeholder(R.drawable.ic_add_video)
                            .into(videoView);
                    updateVideo = true;
                } else {
                    updateVideo = false;
                    Toast.makeText(getActivity(), "Không phải link video youtube", Toast.LENGTH_SHORT).show();
                }
            }

        });
        AlertDialog dialog = editDialog.create();
        dialog.show();
    }


    public static String getYoutubeVideoIdFromUrl(String inUrl) {
        inUrl = inUrl.replace("&feature=youtu.be", "");
        if (inUrl.toLowerCase().contains("youtu.be")) {
            return "http://img.youtube.com/vi/" + inUrl.substring(inUrl.lastIndexOf("/") + 1) + "/0.jpg";
        }
        String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(inUrl);
        if (matcher.find()) {
            return "http://img.youtube.com/vi/" + matcher.group() + "/0.jpg";
        }
        return null;
    }

    public static boolean isYoutubeUrl(String youTubeURl) {
        boolean success;
        String pattern = "^(http(s)?:\\/\\/)?((w){3}.)?youtu(be|.be)?(\\.com)?\\/.+";
        if (!youTubeURl.isEmpty() && youTubeURl.matches(pattern)) {
            success = true;
        } else {
            // Not Valid youtube URL
            success = false;
        }
        return success;
    }

    public static void setData() {
        if (updateVideo)
            CreateRecipeActivity.recipe.setRecipeVideo(videoUrl);
        CreateRecipeActivity.recipe.setRecipeName(editTextName.getText().toString().trim());
        CreateRecipeActivity.recipe.setRecipeRequire(editTextRequire.getText().toString().trim());
        CreateRecipeActivity.recipe.setRecipeLevelOfDifficult(EnumLevelOfDifficult.values()[spnLevelOfDifficult.getSelectedItemPosition()]);
        CreateRecipeActivity.recipe.setRecipeType(EnumRecipeType.values()[spnType.getSelectedItemPosition()]);
        CreateRecipeActivity.recipe.setRecipeStorage(radioButtonLocal.isChecked() ? EnumStorage.PERSONAL : EnumStorage.WAITING);
    }

    public static boolean saveImage(String name) {
        if (updateImage) {
            String path = ImageUtil.saveImageToFile(bitmap, name);
            if (path != null) {
                CreateRecipeActivity.recipe.setRecipeImage(path);
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setImageBitmap(bitmap);
                updateImage = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
