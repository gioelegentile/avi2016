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
import com.gc.materialdesign.views.ButtonFloat;
import com.telly.mrvector.MrVector;

import it.uniba.gruppo10.avi2016.R;
import it.uniba.gruppo10.avi2016.adapters.ChatListAdapter;
import it.uniba.gruppo10.avi2016.entities.Chat;
import it.uniba.gruppo10.avi2016.utilities.IDUtente;

/**
 * E' l'activity di chat di gruppo.
 */
public class GroupChatActivity extends AppCompatActivity {

    private final String FIREBASEURL = "https://scorching-heat-6469.firebaseio.com";
    Firebase ref;
    private ListView list;
    private ChatListAdapter mChatListAdapter;
    private String username;
    private String name;
    private String[] title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_user);
        Firebase.setAndroidContext(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= 21)
            actionBar.setElevation(0);
        name=getIntent().getExtras().getString("Name");
        title=name.split("-");
        setTitle(title[0]);

        username = IDUtente.getNome() + " " + IDUtente.getCognome();
        list = (ListView) findViewById(R.id.message);
        ref = new Firebase(FIREBASEURL).child("chat").child(name).child("messaggi");

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
        ButtonFloat button = (ButtonFloat) findViewById(R.id.sendButton);
        Drawable drawable = MrVector.inflate(getResources(), R.drawable.send);
        button.setDrawableIcon(drawable);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        mChatListAdapter = new ChatListAdapter(ref.limit(50), this, R.layout.chatmessage, username, ChatListAdapter.GROUP);
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
        String input = inputText.getText().toString();
        if (!input.equals("")) {
            // Create our 'model', a Chat object
            Chat chat = new Chat(input, username);
            // Create a new, auto-generated child of that chat location, and save our chat data there
            ref.push().setValue(chat);
            inputText.setText("");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_group_chat, menu);
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
