package com.example.faketagram_app.ui.following;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.faketagram_app.Constant;
import com.example.faketagram_app.FollowResponse;
import com.example.faketagram_app.R;
import com.example.faketagram_app.UserProfileActivity;
import com.example.faketagram_app.UserRvAdapter;
import com.example.faketagram_app.model.Users;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FollowingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FollowingFragment extends Fragment {

    RecyclerView rv;
    SwipeRefreshLayout srl;
    View viewFragment;

    List<FollowResponse> followingsList;
    List<Users> usersList = new ArrayList<>();
    UserRvAdapter adapter;

    int counter = 0;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FollowingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment followingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FollowingFragment newInstance(String param1, String param2) {
        FollowingFragment fragment = new FollowingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewFragment = inflater.inflate(R.layout.fragment_following, container, false);
        srl = viewFragment.findViewById(R.id.srlFollowingFragment);
        rv = viewFragment.findViewById(R.id.rvFollowingsFragment);

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                followingsList.clear();
                usersList.clear();
                initializeFollowings();
                srl.setRefreshing(false);
            }
        });

        initializeFollowings();

        return viewFragment;
    }

    public void initializeFollowings() {
        Call<List<FollowResponse>> usersResponseCall = Constant.CONNECTION.getFollowings(Constant.AUTHTOKEN);
        usersResponseCall.enqueue(new Callback<List<FollowResponse>>() {
            @Override
            public void onResponse(Call<List<FollowResponse>> call, Response<List<FollowResponse>> response) {
                if(response.isSuccessful()){
                    followingsList = response.body();
                    getFollowing();
                } else {
                    Constant.Message(getContext(),"Error-FollowingFragment-initializeFollowings-onResponse");
                }
            }

            @Override
            public void onFailure(Call<List<FollowResponse>> call, Throwable t) {
                Log.d("Error-FollowingFragment-initializeFollowings-onFailure: ", t.getMessage());
                Constant.Message(getContext(),"Error-FollowingFragment-initializeFollowings-onFailure");
            }
        });
    }

    public void getFollowing () {
        for(FollowResponse followAux : followingsList){
            Call<Users> call = Constant.CONNECTION.getUserById(Constant.AUTHTOKEN, followAux.getUser_followed_id());
            call.enqueue(new Callback<Users>() {
                @Override
                public void onResponse(Call<Users> call, Response<Users> response) {
                    if (response.isSuccessful()) {
                        usersList.add(counter, response.body());
                        counter++;
                        buildRV();
                    } else {
                        Constant.Message(getContext(),"Error-FollowingFragment-getFollowings-onResponse");
                    }
                }

                @Override
                public void onFailure(Call<Users> call, Throwable t) {
                    Log.d("Error-FollowingFragment-getFollowings-onFailure: ", t.getMessage());
                    Constant.Message(getContext(),"Error-FollowingFragment-getFollowings-onFailure: " + t.getMessage());
                }
            });
        }
        counter = 0;
    }

    private void buildRV() {
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);

        adapter = new UserRvAdapter(usersList);

        Intent intent = new Intent(getActivity(), UserProfileActivity.class);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("userSelectedId", usersList.get
                        (rv.getChildAdapterPosition(v)).getUser_id());
                intent.putExtra("userSelectedName", usersList.get
                        (rv.getChildAdapterPosition(v)).getName());
                intent.putExtra("userSelectedLastName", usersList.get
                        (rv.getChildAdapterPosition(v)).getLastname());
                intent.putExtra("userSelectedUsername", usersList.get
                        (rv.getChildAdapterPosition(v)).getUsername());
                intent.putExtra("userSelectedEmail", usersList.get
                        (rv.getChildAdapterPosition(v)).getEmail());
                intent.putExtra("userSelectedPassword", usersList.get
                        (rv.getChildAdapterPosition(v)).getPassword());
                intent.putExtra("userSelectedStatus", usersList.get
                        (rv.getChildAdapterPosition(v)).getStatus());
                intent.putExtra("userSelectedCellphone", usersList.get
                        (rv.getChildAdapterPosition(v)).getCellphone());
                intent.putExtra("userSelectedImage", usersList.get
                        (rv.getChildAdapterPosition(v)).getImage_storage_path());
                startActivity(intent);
            }
        });

        rv.setAdapter(adapter);
    }
}