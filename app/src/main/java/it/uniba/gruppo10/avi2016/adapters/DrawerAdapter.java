package it.uniba.gruppo10.avi2016.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.telly.mrvector.MrVector;

import it.uniba.gruppo10.avi2016.R;

/**
 * Adapter  usato per le varie voci selezionabili dall'utente nel NavigationDrawer
 * di {@link it.uniba.gruppo10.avi2016.activities.MenuPrincipaleActivity}
 */
public class DrawerAdapter extends BaseAdapter {

    private Context context;
    private String[] values;
    private Drawable drawable;
    public DrawerAdapter(Context context) {
        this.context = context;
        values = new String[]{
                context.getString(R.string.drawerOptionDay1),
                context.getString(R.string.drawerOptionDay2),
                context.getString(R.string.drawerOptionDay3),
                context.getString(R.string.myevents),
                context.getString(R.string.chat),
                context.getString(R.string.personal_data),
                context.getString(R.string.venue),
                context.getString(R.string.committees),
                context.getString(R.string.contacts),
                context.getString(R.string.about)
        };
    }

    @Override
    public int getCount() {
        return values.length;
    }

    @Override
    public Object getItem(int position) {
        return values[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.drawer_list, null);
            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) convertView.findViewById(R.id.drawer_item_image);
            viewHolder.text = (TextView) convertView.findViewById(R.id.drawer_item_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        switch (position) {
            case 0:
                drawable = MrVector.inflate(convertView.getResources(), R.drawable.numeric_1_box);
                viewHolder.image.setImageDrawable(drawable);
                viewHolder.text.setText(context.getResources().getText(R.string.drawerOptionDay1));
                break;
            case 1:
                drawable = MrVector.inflate(convertView.getResources(), R.drawable.numeric_2_box);
                viewHolder.image.setImageDrawable(drawable);
                viewHolder.text.setText(context.getResources().getText(R.string.drawerOptionDay2));
                break;
            case 2:
                drawable = MrVector.inflate(convertView.getResources(), R.drawable.numeric_3_box);
                viewHolder.image.setImageDrawable(drawable);
                viewHolder.text.setText(context.getResources().getText(R.string.drawerOptionDay3));
                break;
            case 4:
                drawable = MrVector.inflate(convertView.getResources(), R.drawable.message_text);
                viewHolder.image.setImageDrawable(drawable);
                viewHolder.text.setText(context.getResources().getText(R.string.chat));
                break;
            case 3:
                drawable = MrVector.inflate(convertView.getResources(), R.drawable.calendar_check_azure);
                viewHolder.image.setImageDrawable(drawable);
                viewHolder.text.setText(context.getResources().getText(R.string.myevents));
                break;
            case 5:
                drawable = MrVector.inflate(convertView.getResources(), R.drawable.clipboard_account);
                viewHolder.image.setImageDrawable(drawable);
                viewHolder.text.setText(context.getResources().getText(R.string.personal_data));
                break;
            case 6:
                drawable = MrVector.inflate(convertView.getResources(), R.drawable.home);
                viewHolder.image.setImageDrawable(drawable);
                viewHolder.text.setText(context.getResources().getText(R.string.venue));
                break;
            case 7:
                drawable = MrVector.inflate(convertView.getResources(), R.drawable.account_multiple);
                viewHolder.image.setImageDrawable(drawable);
                viewHolder.text.setText(context.getResources().getText(R.string.committees));
                break;
            case 8:
                drawable = MrVector.inflate(convertView.getResources(), R.drawable.clipboard_account);
                viewHolder.image.setImageDrawable(drawable);
                viewHolder.text.setText(context.getString(R.string.contacts));
                break;
            case 9:
                drawable = MrVector.inflate(convertView.getResources(), R.drawable.information_azure);
                viewHolder.image.setImageDrawable(drawable);
                viewHolder.text.setText(context.getResources().getText(R.string.about));
                break;
            default:
        }
        return convertView;
    }

    private class ViewHolder {
        ImageView image;
        TextView text;
    }

}
