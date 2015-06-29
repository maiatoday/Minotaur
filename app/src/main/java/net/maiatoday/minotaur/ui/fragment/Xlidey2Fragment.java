package net.maiatoday.minotaur.ui.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.maiatoday.minotaur.R;
import net.maiatoday.minotaur.ui.activity.OnTwistyInteractionListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnTwistyInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Xlidey2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Xlidey2Fragment  extends TwistyFragment {
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param id Parameter 1.
     * @param name Parameter 2.
     * @return A new instance of fragment Xlidey2Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Xlidey2Fragment newInstance(int id, String name) {
        Xlidey2Fragment fragment = new Xlidey2Fragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, id);
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    public Xlidey2Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_xlidey2, container, false);
        TextView name = (TextView) view.findViewById(R.id.textName);
        name.setText(mName + " " + mId);
        return  view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
}
