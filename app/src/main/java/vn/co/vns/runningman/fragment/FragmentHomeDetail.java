package vn.co.vns.runningman.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import vn.co.vns.runningman.R;

/**
 * Created by thanhnv on 11/28/16.
 */
public class FragmentHomeDetail extends Fragment {
    private static final String KEY_POSITION="position";

    public static FragmentHomeDetail newInstance(int position) {
        FragmentHomeDetail frag=new FragmentHomeDetail();
        Bundle args=new Bundle();

        args.putInt(KEY_POSITION, position);
        frag.setArguments(args);

        return(frag);
    }

    public static String getTitle(Context ctxt, int position) {
        return(String.format("123", position + 1));
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View result=inflater.inflate(R.layout.fragment_price_volume, container, false);
//        EditText editor=(EditText) result.findViewById(R.id.editor);
        int position=getArguments().getInt(KEY_POSITION, -1);

//        editor.setHint(getTitle(getActivity(), position));

        return(result);
    }
}
