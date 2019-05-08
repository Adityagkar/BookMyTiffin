package com.example.bookmytiffin;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragLeft.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragLeft#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragLeft extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    TextView textView,orderDetails,leftTiffin;

    public FragLeft() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragLeft.
     */
    // TODO: Rename and change types and number of parameters
    public static FragLeft newInstance(String param1, String param2) {
        FragLeft fragment = new FragLeft();
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_frag_left, container, false);

        textView = v.findViewById(R.id.nameuser);
        orderDetails = v.findViewById(R.id.zz);
        leftTiffin = v.findViewById(R.id.lefttiffin);
        final OrderData orderData = new OrderData();


        // Inflate the layout for this fragment
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("OrderTable");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i=1;
                StringBuffer finalDisplay= new StringBuffer("My Orders");
                HashMap<Object, Object> map = null;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String emailId = snapshot.getKey();
                    if (emailId.equals(orderData.getId()) ){

                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            String Order = snapshot1.getKey();
                             map = (HashMap<Object, Object>) dataSnapshot.child(emailId).child(Order).getValue();
                            String nameTaken= (String) map.get("Name");
                            String dob = (String) map.get("StartDate");
                            String doe = (String) map.get("EndDate");
                            String tcentre = (String) map.get("TCentre");
                         String temp = "Order "+i+"\n Name:"+nameTaken+"\n Date of Starting "+dob+"\n Date of ending "+doe+"\n Tiffin Centre "+tcentre;
                         finalDisplay.append(temp);

                        }
                        orderDetails.setText(finalDisplay);
                        textView.setText((String) map.get("Mobile"));
                        leftTiffin.setText(map.get("TiffinsLeft")+"");
                        break;
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
