package fragment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.parstagram.Post;
import com.example.parstagram.Profile;
import com.example.parstagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment {

    private static final String TAG = "EditProfileFragment";
    private static int RESULT_LOAD_IMAGE = 1;
    private Button buttonLoadImage;
    private ImageView ivProfileImage;
    private Button btnSavePicture;

    public EditProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivProfileImage = view.findViewById(R.id.ivProfileImage);
        buttonLoadImage = view.findViewById(R.id.buttonLoadPicture);
        btnSavePicture = view.findViewById(R.id.btnSavePicture);
        
        btnSavePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryProfile();
                if(ivProfileImage != null){
                    Profile profile = new Profile();
                    // Convert image view to bitmap
                    BitmapDrawable drawable = (BitmapDrawable) ivProfileImage.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    ParseFile parseFile = conversionBitmapParseFile(bitmap);
                    profile.setProfile(parseFile);
                    profile.setUser(ParseUser.getCurrentUser());

                    Toast.makeText(getContext(), "Username " + profile.getUser().getUsername(), Toast.LENGTH_LONG).show();

                    profile.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e != null){
                                Log.e(TAG, "Error while saving!", e);
                                Toast.makeText(getContext(), "Error while saving!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Log.i(TAG, "Profile save was successfull!!!");
                            ivProfileImage.setImageResource(0);
                        }
                    });

                }
            }
        });
        
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
    }

    private void queryProfile() {
        // Specify which class to query
        ParseQuery<Profile> query = ParseQuery.getQuery(Profile.class);

        query.include(Profile.KEY_USER);
        query.setLimit(1);
        query.whereEqualTo(Profile.KEY_USER, ParseUser.getCurrentUser());
        query.addDescendingOrder(Profile.KEY_CREATED_AT);

        query.findInBackground(new FindCallback<Profile>() {
            @Override
            public void done(List<Profile> profiles, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue with getiing posts, e");
                    return;
                }

                for(Profile profile : profiles){
                   // Log.i(TAG, "Post : " + post.getDescription() + ", username: " + post.getUser().getUsername());
                   // Toast.makeText(getContext(), "Username " + profile.getUser().getUsername(), Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContext().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Glide.with(getContext()).load(selectedImage).into(ivProfileImage);

        }


    }

    //Convert bitmap image to parse file
    public ParseFile conversionBitmapParseFile(Bitmap imageBitmap){
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        ParseFile parseFile = new ParseFile("image_file.png",imageByte);
        return parseFile;
    }
}
