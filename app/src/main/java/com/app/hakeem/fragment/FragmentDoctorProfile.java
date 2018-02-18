package com.app.hakeem.fragment;


import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.hakeem.ActivityContainer;
import com.app.hakeem.R;
import com.app.hakeem.pojo.User;
import com.app.hakeem.util.C;
import com.app.hakeem.util.ImageLoader;
import com.app.hakeem.util.SharedPreference;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDoctorProfile extends Fragment {

    @BindView(R.id.btnCaseHistory)
    Button btnCaseHistory;
    @BindView(R.id.btnManageAwarenessQueue)
    Button btnManageAwarenessQueue;
    @BindView(R.id.btnManageQueue)
    Button btnManageQueue;
    @BindView(R.id.imgProfile)
    ImageView imgProfile;
    @BindView(R.id.tvDocName)
    TextView tvDocName;
    @BindView(R.id.tvDocProf)
    TextView tvDocProf;
    User user;
    ImageLoader imageLoader;
    public FragmentDoctorProfile() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLoader=new ImageLoader(getActivity());
        user= SharedPreference.getInstance(getActivity()).getUser(C.LOGIN_USER);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doctor_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        if(user!=null && user.getUserType().equals(C.DOCTOR)){
            tvDocName.setText(user.getFirstName()+" "+user.getLastName());
            tvDocProf.setText(user.getSpecialist());

            imageLoader.DisplayImage(user.getUserPic(),imgProfile);
        }

        Drawable drawable = getResources().getDrawable(R.drawable.mng_awareness);
        drawable.setBounds(0, 0, (int)(drawable.getIntrinsicWidth()*0.6),
                (int)(drawable.getIntrinsicHeight()*0.6));
        ScaleDrawable sd = new ScaleDrawable(drawable, 0, 98, 98);
        btnManageAwarenessQueue.setCompoundDrawables(sd.getDrawable(), null, null, null);
        btnCaseHistory.setOnClickListener(mBtnCaseHostoryClickListner);
        btnManageAwarenessQueue.setOnClickListener(mbtnManageAwarenessQueueClickListner);
      //  btnManageQueue.setOnClickListener(mBtnCaseHostoryClickListner);

    }

    @Override
    public void onResume() {
        super.onResume();
        ActivityContainer.tvTitle.setText(R.string.doc_profile);

    }

    View.OnClickListener mBtnCaseHostoryClickListner=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((ActivityContainer)getActivity()).fragmnetLoader(C.FRAGMENT_DOCTOR_PATIENT_LIST,null);
        }
    };
    View.OnClickListener mbtnManageAwarenessQueueClickListner=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getActivity().finish();
        }
    };
}
