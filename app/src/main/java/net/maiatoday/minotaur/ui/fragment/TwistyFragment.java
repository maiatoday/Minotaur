package net.maiatoday.minotaur.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

import net.maiatoday.minotaur.R;
import net.maiatoday.minotaur.provider.MContract;
import net.maiatoday.minotaur.ui.activity.OnTwistyInteractionListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnTwistyInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TwistyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TwistyFragment extends Fragment {
    protected static final String ARG_ID = "id";
    protected static final String ARG_NAME = "name";

    protected int mId;
    protected String mName;

    protected OnTwistyInteractionListener mListener;

    protected SimpleCursorAdapter mAdapter;

    protected static final int LOADER_ID = 10;
    protected static final String KEY_ROOM_ID = "room_id";
    protected static final String[] PROJECTION = new String[]{
            MContract.Loot._ID,
            MContract.Loot.COLUMN_NAME,
            MContract.Loot.COLUMN_VALUE,
            MContract.Loot.COLUMN_IS_HERRING
    };


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param id Parameter 1.
     * @param name Parameter 2.
     * @return A new instance of fragment Xlidey1Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TwistyFragment newInstance(int id, String name) {
        TwistyFragment fragment = new TwistyFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, id);
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    public int getRoomId() {
        return getArguments().getInt(ARG_ID, 0);
    }

    public TwistyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mId = getArguments().getInt(ARG_ID, 0);
            mName = getArguments().getString(ARG_NAME);
        }
        mAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_2, null,
                new String[] { MContract.Loot.COLUMN_NAME, MContract.Loot.COLUMN_VALUE },
                new int[] { android.R.id.text1, android.R.id.text2 }, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_xlidey1, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnTwistyInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
