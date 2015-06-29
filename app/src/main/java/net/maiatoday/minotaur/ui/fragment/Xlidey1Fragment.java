package net.maiatoday.minotaur.ui.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import net.maiatoday.minotaur.R;
import net.maiatoday.minotaur.provider.MContract;
import net.maiatoday.minotaur.ui.activity.OnTwistyInteractionListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnTwistyInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Xlidey1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Xlidey1Fragment extends TwistyFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param id Parameter 1.
     * @param name Parameter 2.
     * @return A new instance of fragment Xlidey1Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Xlidey1Fragment newInstance(int id, String name) {
        Xlidey1Fragment fragment = new Xlidey1Fragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, id);
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    public Xlidey1Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_ROOM_ID, mId);
        getLoaderManager().initLoader(LOADER_ID, bundle, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_xlidey1, container, false);
        TextView name = (TextView) view.findViewById(R.id.textName);
        name.setText(mName + " " + mId);
        ListView list = (ListView) view.findViewById(R.id.listview);
        list.setAdapter(mAdapter);
        return  view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
            getActivity(),   // Parent activity context
            MContract.Loot.CONTENT_URI,        // Table to query
            PROJECTION,     // Projection to return
            MContract.Loot.COLUMN_ROOM_ID + " =?",
            new String[] {String.valueOf(args.getInt(KEY_ROOM_ID))},            // No selection arguments
            null             // Default sort order
    );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
