package com.example.faketagram_app.ui.follower;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.faketagram_app.Constant;
import com.example.faketagram_app.FollowResponse;
import com.example.faketagram_app.R;
import com.example.faketagram_app.UserRvAdapter;
import com.example.faketagram_app.model.Users;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FollowerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FollowerFragment extends Fragment {

    RecyclerView rv;
    View viewFragment;
    EditText txtFollowerFragment;
    Button btnFollowerFragment, btnShowFollowerFragment;

    List<FollowResponse> followersList;
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

    public FollowerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FollowerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FollowerFragment newInstance(String param1, String param2) {
        FollowerFragment fragment = new FollowerFragment();
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
        viewFragment = inflater.inflate(R.layout.fragment_follower, container, false);
        rv = viewFragment.findViewById(R.id.rvFollowersFragment);
        txtFollowerFragment = viewFragment.findViewById(R.id.txtSearchFollower);
        btnFollowerFragment = viewFragment.findViewById(R.id.btnSearchFollower);
        btnShowFollowerFragment = viewFragment.findViewById(R.id.btnShowFollower);

        btnFollowerFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!txtFollowerFragment.getText().toString().isEmpty()){

                } else {
                    Constant.Message(getContext(), "Complete the field");
                }
            }
        });

        btnShowFollowerFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initializeFollowers();
                buildRV();
            }
        });

        return viewFragment;
    }

    public void initializeFollowers() {
        Call<List<FollowResponse>> usersResponseCall = Constant.CONNECTION.getFollowers(Constant.AUTHTOKEN);
        usersResponseCall.enqueue(new Callback<List<FollowResponse>>() {
            @Override
            public void onResponse(Call<List<FollowResponse>> call, Response<List<FollowResponse>> response) {
                if(response.isSuccessful()){
                    followersList = response.body();
                    getFollowers();
                } else {
                    Constant.Message(getContext(),"Error-FollowerFragment-initializeFollowers-onResponse");
                }
            }

            @Override
            public void onFailure(Call<List<FollowResponse>> call, Throwable t) {
                Log.d("Error-FollowingFragment-initializeFollowers-onFailure: ", t.getMessage());
                Constant.Message(getContext(),"Error-FollowersFragment-initializeFollowers-onFailure");
            }
        });
    }

    public void getFollowers () {
        for(FollowResponse followAux : followersList){
            Call<Users> call = Constant.CONNECTION.getUserById(Constant.AUTHTOKEN, followAux.getUser_follower_id());
            call.enqueue(new Callback<Users>() {
                @Override
                public void onResponse(Call<Users> call, Response<Users> response) {
                    if (response.isSuccessful()) {
                        usersList.add(counter, response.body());
                        counter++;
                    } else {
                        Constant.Message(getContext(),"Error-FollowersFragment-getFollowers-onResponse");
                    }
                }

                @Override
                public void onFailure(Call<Users> call, Throwable t) {
                    Log.d("Error-FollowingFragment-getFollowers-onFailure: ", t.getMessage());
                    Constant.Message(getContext(),"Error-FollowerFragment-getFollowers-onFailure: " + t.getMessage());
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
        usersList.clear();
        rv.setAdapter(adapter);
    }
}