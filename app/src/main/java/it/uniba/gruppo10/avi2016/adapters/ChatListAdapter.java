package it.uniba.gruppo10.avi2016.adapters;

import android.app.Activity;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Query;

import it.uniba.gruppo10.avi2016.R;
import it.uniba.gruppo10.avi2016.entities.Chat;

/**
 * Adapter per l'activity {@link it.uniba.gruppo10.avi2016.activities.ChatUserActivity}
 * che popola le textview ogni qual volta si scrive o si riceve un messaggio da un altro utente.
 */
public class ChatListAdapter extends FirebaseListAdapter<Chat> {
    public static final int SINGLE = 0;
    public static final int GROUP = 1;
    private String mId;
    private int type;

    public ChatListAdapter(Query ref, Activity activity, int layout, String mId, int type) {
        super(ref, Chat.class, layout, activity);
        this.mId = mId;
        this.type = type;
    }

    /**
     * Bind an instance of the <code>Chat</code> class to our view. This method is called by <code>FirebaseListAdapter</code>
     * when there is a data change, and we are given an instance of a View that corresponds to the layout that we passed
     * to the constructor, as well as a single <code>Chat</code> instance that represents the current data to bind.
     *
     * @param view A view instance corresponding to the layout we passed to the constructor.
     * @param chat An instance representing the current state of a chat message
     */
    @Override
    protected void populateView(View view, Chat chat) {

        String author = chat.getAuthor();
        TextView myMessage = (TextView) view.findViewById(R.id.my_message);
        TextView otherMessage = (TextView) view.findViewById(R.id.other_message);
        Spanned message = null;

        if (this.type == GROUP)
            message = Html.fromHtml("<b><small><sup>" + author + "</sup></small></b>" + "<br />" + chat.getMessage());
        else
            message = Html.fromHtml(chat.getMessage());

        if (author != null && author.equals(mId)) {
            myMessage.setText(chat.getMessage());
            otherMessage.setText("");
            myMessage.setBackgroundResource(R.drawable.mymessage);
            otherMessage.setBackground(null);
            if (android.os.Build.VERSION.SDK_INT <= 18) {
                myMessage.setPadding(13,5,26,6);
            }
        } else {
            myMessage.setBackground(null);
            myMessage.setText("");
            otherMessage.setText(message);
            otherMessage.setBackgroundResource(R.drawable.othermessage);
            if (android.os.Build.VERSION.SDK_INT <= 18) {
                otherMessage.setPadding(26,5,13,6);
            }
        }

    }
}