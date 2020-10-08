package com.carlos.minitwitter.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.carlos.minitwitter.R;
import com.carlos.minitwitter.data.TweetViewModel;

public class NewTweetFragment extends DialogFragment implements View.OnClickListener {

    private EditText edTweetMessage;
    private ImageView iCloseNewTweet;
    private Button bCreateNewTweet;
    private TweetViewModel tweetViewModel;

    public NewTweetFragment() {}

    /**
     *
     * metodo se ejecuta al crear el fragment
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog);

        tweetViewModel = new ViewModelProvider(getActivity()).get(TweetViewModel.class);
    }

    /**
     *
     * metodo se ejecuta al crear la vista en el fragment
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_tweet_new, container, false);

        edTweetMessage = view.findViewById(R.id.edTweetMessage);
        iCloseNewTweet = view.findViewById(R.id.iCloseNewTweet);
        bCreateNewTweet = view.findViewById(R.id.bCreateNewTweet);

        iCloseNewTweet.setOnClickListener(this);
        bCreateNewTweet.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iCloseNewTweet:
                if(!edTweetMessage.getText().toString().isEmpty()) {
                    showDialogConfirm();
                } else {
                    dismiss();
                }
                break;
            case R.id.bCreateNewTweet:

                String message = edTweetMessage.getText().toString();

                if(message.isEmpty()) {
                    Toast.makeText(getActivity(), "Debes escribir un mensaje", Toast.LENGTH_SHORT).show();
                    return;
                }

                tweetViewModel.createNewTweet(message);
                dismiss();
                break;
        }
    }

    private void showDialogConfirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
            .setTitle("Cancelar tweet")
            .setMessage("¿Deseas cancelar la publicación de tu tweet?");

        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss(); //cierro la ventana de confirmacion
                getDialog().dismiss(); //cierro el dialogo de cracion de tweets
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
