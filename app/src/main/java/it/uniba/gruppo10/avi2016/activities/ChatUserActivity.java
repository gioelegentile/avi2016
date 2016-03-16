package it.uniba.gruppo10.avi2016.activities;

import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonFloat;
import com.telly.mrvector.MrVector;

import it.uniba.gruppo10.avi2016.R;
import it.uniba.gruppo10.avi2016.adapters.ChatListAdapter;
import it.uniba.gruppo10.avi2016.entities.Chat;
import it.uniba.gruppo10.avi2016.utilities.IDUtente;

/**
 * Questa activity contiene la chat personale tra due utenti.
 */
public class ChatUserActivity extends AppCompatActivity {
    private final String FIREBASEURL = "https://scorching-heat-6469.firebaseio.com";
    Firebase ref, ref2;
    private String idDestinatario, username;
    private ListView list;
    private ChatListAdapter mChatListAdapter;
    private ButtonFloat send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_user);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= 21)
            actionBar.setElevation(0);
        Bundle dati = getIntent().getExtras();
        idDestinatario = dati.getString("ID");
        username = IDUtente.getNome() + " " + IDUtente.getCognome();
        setTitle(dati.getString("Name"));
        Firebase.setAndroidContext(this);
        list = (ListView) findViewById(R.id.message);
        ref = new Firebase(FIREBASEURL).child("chat").child(IDUtente.getId() + idDestinatario);
        ref2 = new Firebase(FIREBASEURL).child("chat").child(idDestinatario + IDUtente.getId());

        EditText inputText = (EditText) findViewById(R.id.messageInput);
        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    sendMessage();
                }
                return true;
            }
        });

        send=(ButtonFloat)findViewById(R.id.sendButton);
        Drawable drawable = MrVector.inflate(getResources(), R.drawable.send);
        send.setDrawableIcon(drawable);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        mChatListAdapter = new ChatListAdapter(ref.limit(50), this, R.layout.chatmessage, username, ChatListAdapter.SINGLE);
        list.setAdapter(mChatListAdapter);
        mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                list.setSelection(mChatListAdapter.getCount() - 1);
            }
        });
    }

    private void sendMessage() {
        EditText inputText = (EditText) findViewById(R.id.messageInput);
        ButtonFloat button = (ButtonFloat) findViewById(R.id.sendButton);
        Drawable drawable = MrVector.inflate(getResources(), R.drawable.send);
        button.setDrawableIcon(drawable);
        String input = inputText.getText().toString();
        if (!input.equals("")) {
            // Create our 'model', a Chat object
            Chat chat = new Chat(input, username);
            // Create a new, auto-generated child of that chat location, and save our chat data there
            inputText.setText("");
            ref.push().setValue(chat);
            ref2.push().setValue(chat);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat_user, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
