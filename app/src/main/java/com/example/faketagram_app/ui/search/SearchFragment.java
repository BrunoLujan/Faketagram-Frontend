package com.example.faketagram_app.ui.search;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.telephony.mbms.MbmsErrors;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebViewFragment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.faketagram_app.Constant;
import com.example.faketagram_app.R;
import com.example.faketagram_app.UserProfileActivity;
import com.example.faketagram_app.UserRvAdapter;
import com.example.faketagram_app.interfaces.ApiService;
import com.example.faketagram_app.model.Users;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    View viewFragment;
    RecyclerView rv;

    EditText txtSearchFragment;
    Button btnSearchFragment;

    List<Users> usersList;
    UserRvAdapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        viewFragment = inflater.inflate(R.layout.fragment_search, container, false);

        rv = viewFragment.findViewById(R.id.rvSearchFragment);
        txtSearchFragment = viewFragment.findViewById(R.id.txtSearch);
        btnSearchFragment = viewFragment.findViewById(R.id.btnSearch);

        btnSearchFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!txtSearchFragment.getText().toString().isEmpty()){
                    searchUsers();
                } else {
                    Constant.Message(getContext(), "Complete the field");
                }
            }
        });

        return viewFragment;
    }

    public void searchUsers() {
        Call<List<Users>> usersResponseCall = Constant.CONNECTION.getSearchUsers(Constant.AUTHTOKEN, txtSearchFragment.getText().toString());
        usersResponseCall.enqueue(new Callback<List<Users>>() {
            @Override
            public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {
                if(response.isSuccessful()){
                    usersList = response.body();
                    buildRV();
                    for(Users userAux : usersList){
                        Log.d("SearchFragment", userAux.getName());
                    }
                } else {
                    Constant.Message(getContext(),"AQUÍ");
                }
            }

            @Override
            public void onFailure(Call<List<Users>> call, Throwable t) {
                Constant.Message(getContext(),"onFailure-SearchUsers-SearchFragment");
            }
        });
    }

    private void buildRV() {
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
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