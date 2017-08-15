package com.wallace.tools.ui.card;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wallace.tools.R;

/**
 * Created by akmob on 2017/8/15.
 */

public class NCardFragment extends Fragment{

    private static final String PARAM_INDEX = "INDEX";

    private int index = 0;
    private TextView tvName;
    private EditText etCard;
//    private ProgressBar progressBar;

    private CardInputListener listener;

    public void setListener(CardInputListener listener) {
        this.listener = listener;
    }

    public static NCardFragment newInstance(int index){
        NCardFragment fragment = new NCardFragment();
        Bundle args = new Bundle();
        args.putInt(PARAM_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            index = getArguments().getInt(PARAM_INDEX);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_n_card, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        tvName = (TextView) view.findViewById(R.id.tvCard_name);
        etCard = (EditText) view.findViewById(R.id.etCard);

        switch (index){
            case 0:
                tvName.setText("CARD NUMBER");
                etCard.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
                break;
            case 1:
                tvName.setText("CARD DATE");
                etCard.setInputType(EditorInfo.TYPE_CLASS_DATETIME);
                break;
            case 2:
                tvName.setText("CVV");
                etCard.setInputType(EditorInfo.TYPE_CLASS_TEXT);
                break;
            case 3:
                tvName.setText("NAME");
                etCard.setInputType(EditorInfo.TYPE_CLASS_TEXT);
                break;
        }
        etCard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(listener != null){
                    listener.inputText(s);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}
