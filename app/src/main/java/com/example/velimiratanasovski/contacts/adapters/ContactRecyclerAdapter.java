package com.example.velimiratanasovski.contacts.adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.velimiratanasovski.contacts.R;
import java.util.ArrayList;
import java.util.List;
import com.example.velimiratanasovski.contacts.model.Contact;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

public class ContactRecyclerAdapter extends RecyclerView.Adapter<ContactRecyclerAdapter.ViewHolder> {

    private ItemClickListener mListener;
    private LayoutInflater mInflater;
    private List<Contact> mContacts;
    private ArrayList<Integer> mSelectedItems = new ArrayList<>();

    public List<Contact> getContacts() {
        return mContacts;
    }

    public ContactRecyclerAdapter(Context context, ItemClickListener listener) {
        mInflater = LayoutInflater.from(context);
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.recycle_contact_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        if (mContacts == null) {
            return;
        }
        Contact contact = mContacts.get(i);

        if (contact != null) {
            viewHolder.bind(contact, i, mListener);
        }

        if (mSelectedItems.contains(i)) {
            viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
            viewHolder.mButtonCall.setBackgroundColor(Color.LTGRAY);
        } else {
            viewHolder.itemView.setBackgroundColor(Color.WHITE);
            viewHolder.mButtonCall.setBackgroundColor(Color.WHITE);
        }
    }

    public void setContacts(List<Contact> mContacts) {
        this.mContacts = mContacts;
        notifyDataSetChanged();
    }

    public void clearSelectedItems(){
        mSelectedItems.clear();
        notifyDataSetChanged();
    }

    public void selectItem(int position) {
        if (mSelectedItems.contains(position)) {
            mSelectedItems.remove(mSelectedItems.indexOf(position));
        } else {
            mSelectedItems.add(position);
        }
        notifyDataSetChanged();
    }

    public int getItemPosition(Contact contact) {
        return mContacts.indexOf(contact);
    }

    public ArrayList<Integer> getSelectedItems() {
        return mSelectedItems;
    }

    public List<Contact> getSelectedContacts() {
        List<Contact> list = new ArrayList<>();
        for(Integer position : mSelectedItems){
            list.add(mContacts.get(position));
        }
        return  list;
    }

    public void setSelectedItems(List<Integer> selectedItems){
        mSelectedItems.addAll(selectedItems);
    }

    @Override
    public int getItemCount() {
        if (mContacts != null)
            return mContacts.size();
        return 0;
    }

    public interface ItemClickListener {
        void OnItemClick(Contact contact);
        void OnLongItemClick(int position);
        void OnCallButtonClick(String number);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mNameAndLastName;
        private ImageButton mButtonCall;
        private CircleImageView mAccountAvatar;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mNameAndLastName = itemView.findViewById(R.id.name_and_lastName);
            mButtonCall = itemView.findViewById(R.id.button_call);
            mAccountAvatar = itemView.findViewById(R.id.contactAvatar);
        }

        void bind(final Contact contact, final int position, final ItemClickListener listener) {

            String text = contact.getName() + " " + contact.getLastName();
            Picasso.get().load(Uri.parse(contact.getAvatar())).placeholder(R.drawable.account_icon).into(mAccountAvatar);

            this.mNameAndLastName.setText(text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnItemClick(contact);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.OnLongItemClick(position);
                    return true;
                }
            });
            mButtonCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnCallButtonClick(contact.getPhoneNumber());
                }
            });
        }
    }
}
