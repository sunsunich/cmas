package org.cmas.android.ui.file;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import org.cmas.BaseBeanContainer;
import org.cmas.android.MainActivity;
import org.cmas.android.i18n.ErrorCodesManager;
import org.cmas.android.ui.signin.PostToServerService;
import org.cmas.android.ui.signin.RegistrationFormObject;
import org.cmas.android.ui.signin.RegistrationSuccessFragment;
import org.cmas.ecards.R;
import org.cmas.ecards.databinding.FileAdditionFragmentBinding;
import org.cmas.remote.ErrorCodes;
import org.cmas.util.android.CreateImageThumbnailInput;
import org.cmas.util.android.TaskViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileAdditionFragment extends Fragment {

    protected static final int PICK_PHOTO_ACTION = 113;
    protected static final int MAKE_PHOTO_ACTION = 114;

    private final ErrorCodesManager errorCodesManager = BaseBeanContainer.getInstance().getErrorCodesManager();

    private FileAdditionFragmentBinding binding;
    private ImagesAdapter imagesAdapter;

    private CreateImageFileViewModel createImageFileViewModel;
    private CopyImageFileViewModel copyImageFileViewModel;
    private CreateImageThumbnailViewModel createImageThumbnailViewModel;
    private PathToThumbnailStorage pathToThumbnailStorage;

    private RegistrationFormObject registrationFormObject;
    private boolean fileUploadInProgress;

    @Nullable
    private List<String> newImagePaths;

    public static FileAdditionFragment newInstance(RegistrationFormObject registrationFormObject) {
        FileAdditionFragment fileAdditionFragment = new FileAdditionFragment();
        fileAdditionFragment.registrationFormObject = registrationFormObject;
        return fileAdditionFragment;
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        ComponentManager.getInstance().getApplicationComponent().inject(this);
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        pathToThumbnailStorage = TaskViewModel.safelyInitTask(this, PathToThumbnailStorage.class, true);
        createImageFileViewModel = TaskViewModel.safelyInitTask(this, CreateImageFileViewModel.class, true);
        copyImageFileViewModel = TaskViewModel.safelyInitTask(this, CopyImageFileViewModel.class, true);
        createImageThumbnailViewModel = TaskViewModel.safelyInitTask(this, CreateImageThumbnailViewModel.class, true);

        binding = DataBindingUtil.inflate(inflater, R.layout.file_addition_fragment, container, false);

        binding.images.setLayoutManager(new LinearLayoutManager(requireContext()));
        imagesAdapter = new ImagesAdapter(pathToThumbnailStorage);
        binding.images.setAdapter(imagesAdapter);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        FragmentActivity activity = requireActivity();
        LifecycleOwner viewLifecycleOwner = getViewLifecycleOwner();
        activity.getOnBackPressedDispatcher().addCallback(
                viewLifecycleOwner, new OnBackPressedCallback(fileUploadInProgress) {
                    @Override
                    public void handleOnBackPressed() {
                        activity.finish();
                    }
                }
        );

        createImageFileViewModel.getResult().observe(viewLifecycleOwner, result -> {
            if (result == null) {
                MainActivity.reportError(activity, getString(R.string.error_image_creation));
            } else {
                pathToThumbnailStorage.setLastAddedImagePath(result.getAbsolutePath());
                dispatchTakePictureIntent(result);
            }
        });
        copyImageFileViewModel.getResult().observe(viewLifecycleOwner, result -> {
            File file = result.getRight();
            if (file == null) {
                MainActivity.reportError(activity, getString(result.getLeft()));
            } else {
                String imagePath = file.getAbsolutePath();
                pathToThumbnailStorage.setLastAddedImagePath(imagePath);
                int maxImageWidth = getThumbnailWidth();
                createImageThumbnailViewModel.start(
                        new CreateImageThumbnailInput(false, imagePath, maxImageWidth, maxImageWidth)
                );
            }
        });
        createImageThumbnailViewModel.getResult().observe(viewLifecycleOwner, result -> {
            Bitmap bitmap = result.getRight();
            String lastAddedImagePath = pathToThumbnailStorage.getLastAddedImagePath();
            if (bitmap == null) {
                MainActivity.reportError(requireActivity(), getString(result.getLeft()));
            } else {
                if (lastAddedImagePath != null) {
                    pathToThumbnailStorage.addImage(lastAddedImagePath);
                    imagesAdapter.addImage(bitmap);
                    binding.images.scrollToPosition(imagesAdapter.getItemCount() - 1);
                }
            }
        });
        pathToThumbnailStorage.getResult().observe(viewLifecycleOwner, result -> {
            if(newImagePaths == null) {
                restoreImageState(result);
                toggleLoader(false);
            } else {
                pathToThumbnailStorage.resetImages(newImagePaths);
                pathToThumbnailStorage.start(getThumbnailWidth());
                newImagePaths = null;
            }
        });

        BaseBeanContainer.getInstance().getPostToServiceResultLiveData().observe(viewLifecycleOwner, result -> {
            if (result == null) {
                toggleLoader(true);
                fileUploadInProgress = true;
            } else {
                fileUploadInProgress = false;
                toggleLoader(false);
                Intent serviceIntent = new Intent(requireContext(), PostToServerService.class);
                serviceIntent.setAction(PostToServerService.STOP_FOREGROUND_ACTION);
                ContextCompat.startForegroundService(requireContext(), serviceIntent);
                if (result.success) {
                    MainActivity.gotoFragment(requireActivity(), new RegistrationSuccessFragment());
                } else {
                    toggleLoader(true);
                    if (!pathToThumbnailStorage.inProgress()) {
                        pathToThumbnailStorage.resetImages(result.initialFilePaths);
                        pathToThumbnailStorage.start(getThumbnailWidth());
                    } else {
                        newImagePaths = result.initialFilePaths;
                    }
                    String errorMessage = errorCodesManager.getByCode(result.errorCode);
                    if (errorMessage == null) {
                        errorMessage = errorCodesManager.getByCode(ErrorCodes.TOTAL_IMAGE_SIZE_TOO_BIG);
                    }
                    MainActivity.reportError(requireActivity(), errorMessage);
                }
            }
        });
        binding.images.addOnLayoutChangeListener((v, left, top, right, bottom, leftWas, topWas, rightWas, bottomWas) -> {
            int widthWas = rightWas - leftWas; // Right exclusive, left inclusive
            if (v.getWidth() != widthWas) {
                pathToThumbnailStorage.start(getThumbnailWidth());
            }
        });

        setupButtons();
    }

    private void restoreImageState(List<Bitmap> thumbnails) {
        imagesAdapter.clear();
        for (Bitmap thumbnail : thumbnails) {
            imagesAdapter.addImage(thumbnail);
        }
    }

    private void setupButtons() {
        binding.bntBack.setOnClickListener((v) -> getParentFragmentManager().popBackStackImmediate());

        binding.bntRegister.setOnClickListener((v) -> {
            Integer errorResource = validateForm();
            if (errorResource == null) {
                toggleLoader(true);
                fileUploadInProgress = true;

                Intent serviceIntent = new Intent(requireContext(), PostToServerService.class);
                serviceIntent.setAction(PostToServerService.START_FOREGROUND_ACTION);
                serviceIntent.putExtra(PostToServerService.REGISTRATION_FORM_OBJECT_KEY, registrationFormObject);
                List<ImagePathAndSize> images = pathToThumbnailStorage.getImages();
                ArrayList<String> filePaths = new ArrayList<>(images.size());
                for (ImagePathAndSize image : images) {
                    filePaths.add(image.path);
                }
                serviceIntent.putStringArrayListExtra(PostToServerService.REGISTRATION_FILE_PATH_KEY, filePaths);
                ContextCompat.startForegroundService(requireContext(), serviceIntent);
            } else {
                MainActivity.reportError(getActivity(), getString(errorResource));
            }
        });

        binding.addImage.setOnClickListener((v) -> {
            FragmentActivity context = getActivity();
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.image_select_dialog);
            Button fromCamera = dialog.findViewById(R.id.btn1);
            fromCamera.setOnClickListener(v12 -> {
                createImageFileViewModel.start(null);
                dialog.dismiss();
            });
            Button fromGallery = dialog.findViewById(R.id.btn2);
            fromGallery.setOnClickListener(v1 -> {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                photoPickerIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(photoPickerIntent, PICK_PHOTO_ACTION);
                dialog.dismiss();
            });
            dialog.findViewById(R.id.cancel).setOnClickListener(v13 -> dialog.dismiss());
            dialog.show();
        });
    }

    private void dispatchTakePictureIntent(File photoFile) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri photoURI = FileProvider.getUriForFile(requireContext(),
                                                  "org.cmas.android.fileprovider",
                                                  photoFile);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(takePictureIntent, MAKE_PHOTO_ACTION);
    }

    @StringRes
    private Integer validateForm() {
        if (pathToThumbnailStorage.getImages().isEmpty()) {
            return R.string.error_images_too_few;
        }
        if (!pathToThumbnailStorage.validateTotalSize()) {
            return R.string.error_images_too_many;
        }
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MAKE_PHOTO_ACTION:
                if (resultCode == Activity.RESULT_OK) {
                    String lastAddedImagePath = pathToThumbnailStorage.getLastAddedImagePath();
                    if (lastAddedImagePath != null) {
                        int maxImageWidth = getThumbnailWidth();
                        createImageThumbnailViewModel.start(
                                new CreateImageThumbnailInput(true, lastAddedImagePath, maxImageWidth, maxImageWidth)
                        );
                    }
                }
                break;
            case PICK_PHOTO_ACTION:
                if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
                    Uri selectedImageUri = data.getData();
                    copyImageFileViewModel.start(selectedImageUri);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private int getThumbnailWidth() {
        Context context = requireContext();
        return (int) ((double)binding.images.getWidth() / 1.5d)
               - context.getDrawable(R.drawable.button_delete).getIntrinsicWidth()
               - (int) (context.getResources().getDimension(R.dimen.checkbox_text_padding) * 2);
    }

    private void toggleLoader(boolean visible) {
        if (visible) {
            binding.loadingPanel.setVisibility(View.VISIBLE);
            binding.controlsPanel.setVisibility(View.GONE);
        } else {
            binding.loadingPanel.setVisibility(View.GONE);
            binding.controlsPanel.setVisibility(View.VISIBLE);
        }
    }
}
