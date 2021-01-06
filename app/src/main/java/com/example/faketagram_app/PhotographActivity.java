package com.example.faketagram_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.faketagram_app.model.Comments;
import com.example.faketagram_app.model.Photographs;
import com.squareup.picasso.Picasso;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotographActivity extends AppCompatActivity {

    Intent intent;
    RecyclerView rv;
    CommentRvAdapter adapter;

    Photographs photographSelected = new Photographs();
    List<Comments> commentsList;

    ImageView ivImagePhotograph;
    TextView txtLikesPhotograph;
    EditText txtCommentPhotograph;
    Button btnLikePhotograph, btnAddToFavourites, btnSendComment;

    int buttonLikeCount = 1;
    int buttonFavouriteCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photograph);

        rv = (RecyclerView) findViewById(R.id.rvPhotograph);
        ivImagePhotograph = (ImageView) findViewById(R.id.ivImagePhotograph);
        txtLikesPhotograph = (TextView) findViewById(R.id.txtLikesPhotograph);
        txtCommentPhotograph = (EditText) findViewById(R.id.txtCommentPhotograph);
        btnLikePhotograph = (Button) findViewById(R.id.btnLikePhotograph);
        btnAddToFavourites = (Button) findViewById(R.id.btnAddToFavouritePhotograph);
        btnSendComment = (Button) findViewById(R.id.btnSendCommentPhotograph);

        initializePhotographSelected();
        initializePhotographActivity();
        validateLike();
        validateOnFavourites();

        btnLikePhotograph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonLikeCount == 0) {
                    dislike();
                } else {
                    like();
                }
            }
        });

        btnAddToFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonFavouriteCount == 0) {
                    deleteFromFavourites();
                } else {
                    addToFavourites();
                }
            }
        });

        btnSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!txtCommentPhotograph.getText().toString().isEmpty()) {
                    sendComment();
                } else {
                    Constant.Message(getApplicationContext(), "Write comment first");
                }
            }
        });
    }

    public void initializePhotographSelected() {
        intent = getIntent();
        photographSelected.setPhotograph_id(intent.getIntExtra("photographSelectedId", 0));
        photographSelected.setPublish_date(intent.getStringExtra("photographSelectedPublishDate"));
        photographSelected.setImage_storage_path(intent.getStringExtra("photographSelectedImageStoragePath"));
        photographSelected.setUser_id(intent.getIntExtra("photographUserId", 0));
    }

    public void initializePhotographActivity() {
        if (photographSelected.getImage_storage_path() != null) {
            Picasso.get().load(Constant.PROFILEIMAGE + photographSelected.getImage_storage_path()).fit().into(ivImagePhotograph);
        }
        getLikes();
        getComments();
    }

    public  void validateLike() {
        Call<List<FavouriteResponse>> call = Constant.CONNECTION.getLikesByUserId(Constant.AUTHTOKEN, Constant.LOGGEDUSER.getUser_id());
        call.enqueue(new Callback<List<FavouriteResponse>>() {
            @Override
            public void onResponse(Call<List<FavouriteResponse>> call, Response<List<FavouriteResponse>> response) {
                if (response.isSuccessful()) {
                    for(FavouriteResponse favouriteAux : response.body()){
                        if(favouriteAux.getPhotograph_id() == photographSelected.getPhotograph_id()) {
                            initializeLikeOnButton();
                        }
                    }
                } else {
                    Constant.Message(getApplicationContext(), "Error, try again: " + response.message());
                    Log.d("ERROR-PhotographActivity-validateLike-onResponse", response.message());
                }
            }

            @Override
            public void onFailure(Call<List<FavouriteResponse>> call, Throwable t) {
                Constant.Message(getApplicationContext(), t.getMessage());
                Log.d("ERROR-PhotographActivity-validateLike-onFailure", t.getMessage());
            }
        });
    }

    public  void validateOnFavourites() {
        Call<List<FavouriteResponse>> call = Constant.CONNECTION.getFavourites(Constant.AUTHTOKEN);
        call.enqueue(new Callback<List<FavouriteResponse>>() {
            @Override
            public void onResponse(Call<List<FavouriteResponse>> call, Response<List<FavouriteResponse>> response) {
                if (response.isSuccessful()) {
                    for(FavouriteResponse favouriteAux : response.body()){
                        if(favouriteAux.getPhotograph_id() == photographSelected.getPhotograph_id()) {
                            initializeFavoriteOnButton();
                        }
                    }
                } else {
                    Constant.Message(getApplicationContext(), "Error, try again: " + response.message());
                    Log.d("ERROR-PhotographActivity-validateOnFavourites-onResponse", response.message());
                }
            }

            @Override
            public void onFailure(Call<List<FavouriteResponse>> call, Throwable t) {
                Constant.Message(getApplicationContext(), t.getMessage());
                Log.d("ERROR-PhotographActivity-validateOnFavourites-onFailure", t.getMessage());
            }
        });
    }

    private void initializeLikeOnButton() {
        Drawable img = getApplicationContext().getResources().getDrawable(R.drawable.ic_image_like);
        btnLikePhotograph.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
        buttonLikeCount = 0;
    }

    private void initializeDislikeOnButton() {
        Drawable img = getApplicationContext().getResources().getDrawable(R.drawable.ic_image_nonlike);
        btnLikePhotograph.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
        buttonLikeCount = 1;
    }

    private void initializeFavoriteOnButton() {
        Drawable img = getApplicationContext().getResources().getDrawable(R.drawable.ic_image_favourite);
        btnAddToFavourites.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
        buttonFavouriteCount = 0;
    }

    private void initializeNoFavoriteOnButton() {
        Drawable img = getApplicationContext().getResources().getDrawable(R.drawable.ic_image_nonfavourite);
        btnAddToFavourites.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
        buttonFavouriteCount = 1;
    }

    private void like() {
        Call<ResponseBody> call = Constant.CONNECTION.like(Constant.AUTHTOKEN, photographSelected.getPhotograph_id());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    initializeLikeOnButton();
                    getLikes();
                    Constant.Message(getApplicationContext(), "Like");
                } else {
                    Constant.Message(getApplicationContext(), "Error, try again: " + response.message());
                    Log.d("ERROR-PhotographActivity-like-onResponse", response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Constant.Message(getApplicationContext(), t.getMessage());
                Log.d("ERROR-PhotographActivity-like-onFailure", t.getMessage());
            }
        });
    }

    private void dislike() {
        Call<ResponseBody> call = Constant.CONNECTION.dislike(Constant.AUTHTOKEN, photographSelected.getPhotograph_id());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    initializeDislikeOnButton();
                    getLikes();
                    Constant.Message(getApplicationContext(), "Dislike");
                } else {
                    Constant.Message(getApplicationContext(), "Error, try again: " + response.message());
                    Log.d("ERROR-PhotographActivity-dislike-onResponse", response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Constant.Message(getApplicationContext(), t.getMessage());
                Log.d("ERROR-PhotographActivity-dislike-onFailure", t.getMessage());
            }
        });
    }

    private void addToFavourites() {
        Call<ResponseBody> call = Constant.CONNECTION.addToFavorites(Constant.AUTHTOKEN, photographSelected.getPhotograph_id());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    initializeFavoriteOnButton();
                    Constant.Message(getApplicationContext(), "Photo has been added to favourites");
                } else {
                    Constant.Message(getApplicationContext(), "Error, try again: " + response.message());
                    Log.d("ERROR-PhotographActivity-addToFavourites-onResponse", response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Constant.Message(getApplicationContext(), t.getMessage());
                Log.d("ERROR-PhotographActivity-addToFavourites-onFailure", t.getMessage());
            }
        });
    }

    private void deleteFromFavourites() {
        Call<ResponseBody> call = Constant.CONNECTION.deleteFromFavourites(Constant.AUTHTOKEN, photographSelected.getPhotograph_id());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    initializeNoFavoriteOnButton();
                    Constant.Message(getApplicationContext(), "Photo has been deleted from favourites");
                } else {
                    Constant.Message(getApplicationContext(), "Error, try again: " + response.message());
                    Log.d("ERROR-PhotographActivity-deleteFromFavourites-onResponse", response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Constant.Message(getApplicationContext(), t.getMessage());
                Log.d("ERROR-PhotographActivity-deleteFromFavourites-onFailure", t.getMessage());
            }
        });
    }

    private void getLikes() {
        Call<String> call = Constant.CONNECTION.getLikesByPhotographId(Constant.AUTHTOKEN, photographSelected.getPhotograph_id());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                 if (response.isSuccessful()) {
                    txtLikesPhotograph.setText(response.body() + " likes");
                    txtLikesPhotograph.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
                } else {
                     Constant.Message(getApplicationContext(), "Error, try again: " + response.message());
                     Log.d("ERROR-PhotographActivity-getLikes-onResponse", response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Constant.Message(getApplicationContext(), t.getMessage());
                Log.d("ERROR-PhotographActivity-getLikes-onFailure", t.getMessage());
            }
        });
    }

    private void getComments() {
        Call<List<Comments>> call = Constant.CONNECTION.getCommentsByPhotographId(Constant.AUTHTOKEN, photographSelected.getPhotograph_id());
        call.enqueue(new Callback<List<Comments>>() {
            @Override
            public void onResponse(Call<List<Comments>> call, Response<List<Comments>> response) {
                if (response.isSuccessful()) {
                    commentsList = response.body();
                    buildRV();
                } else {
                    Constant.Message(getApplicationContext(), "Error, try again: " + response.message());
                    Log.d("ERROR-PhotographActivity-getComments-onResponse", response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Comments>> call, Throwable t) {
                Constant.Message(getApplicationContext(), t.getMessage());
                Log.d("ERROR-PhotographActivity-getComments-onFailure", t.getMessage());
            }
        });
    }

    private void sendComment() {
        CommentRequest comment = new CommentRequest();
        comment.setComment(txtCommentPhotograph.getText().toString());
        Call<ResponseBody> call = Constant.CONNECTION.commentPhotograph(Constant.AUTHTOKEN, comment, photographSelected.getPhotograph_id());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Constant.Message(getApplicationContext(), "Comment has been added");
                    getComments();
                    txtCommentPhotograph.setText("");
                } else {
                    Constant.Message(getApplicationContext(), "Error, try again: " + response.message());
                    Log.d("ERROR-PhotographActivity-sendComment-onResponse", response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Constant.Message(getApplicationContext(), t.getMessage());
                Log.d("ERROR-PhotographActivity-sendComment-onFailure", t.getMessage());
            }
        });
    }

    private void buildRV() {
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(llm);
        adapter = new CommentRvAdapter(commentsList);
        rv.setAdapter(adapter);
    }
}