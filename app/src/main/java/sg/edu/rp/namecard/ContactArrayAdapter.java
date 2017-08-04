package sg.edu.rp.namecard;

/**
 * Created by 15017185 on 30/7/2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;



public class ContactArrayAdapter extends ArrayAdapter<Contact> {

    private ArrayList<Contact> contacts;

    public ContactArrayAdapter(Context context, int resource, ArrayList<Contact> objects) {
        super(context, resource, objects);
        contacts = objects;
    }

    public void setData(ArrayList<Contact> objects){
        this.contacts = objects;
        notifyDataSetChanged();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact_row, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.tvPhone = (TextView) convertView.findViewById(R.id.tvPhone);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(contacts != null && contacts.size() != 0 && contacts.get(position) != null){
            Contact contact = contacts.get(position);
            viewHolder.tvName.setText(contact.name);
            viewHolder.tvPhone.setText(contact.mobile);
        }

        return convertView;
    }

    private static class ViewHolder{
        private TextView tvName;
        private TextView tvPhone;
    }
}
