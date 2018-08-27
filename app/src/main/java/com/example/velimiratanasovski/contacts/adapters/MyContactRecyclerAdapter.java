package com.example.velimiratanasovski.contacts.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.velimiratanasovski.contacts.R;
import java.util.List;
import com.example.velimiratanasovski.contacts.model.Contact;


public class MyContactRecyclerAdapter extends RecyclerView.Adapter<MyContactRecyclerAdapter.ViewHolder> {

    private ItemClickListener mListener;
    private LayoutInflater mInflater;
    private List<Contact> mContacts;

    public MyContactRecyclerAdapter(Context context, ItemClickListener listener) {
        mInflater = LayoutInflater.from(context);
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.recycle_contact_list, viewGroup, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        if (mContacts == null) {
            return;
        }
        Contact contact = mContacts.get(i);

        if (contact != null) {
            viewHolder.bind(contact, mListener);
        }

    }

    public void setContacts(List<Contact> mContacts) {
        this.mContacts = mContacts;
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        if (mContacts != null)
            return mContacts.size();
        return 0;
    }

    public interface ItemClickListener {
        void OnItemClick(Contact contact);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mNameAndLastName;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mNameAndLastName = itemView.findViewById(R.id.name_and_lastName);
        }

        void bind(final Contact contact, final ItemClickListener listener) {

            String text = contact.getName() + " " + contact.getLastName();
            this.mNameAndLastName.setText(text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnItemClick(contact);
                }
            });

        }
    }
}
