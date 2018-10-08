package e.q.represent;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<ContactInfo> contactList;

    public ContactAdapter(List<ContactInfo> contactList) {
        this.contactList = contactList;
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        final ContactInfo ci = contactList.get(i);
        contactViewHolder.vName.setText(ci.name);
        contactViewHolder.vParty.setText(ci.party);
        contactViewHolder.vDistrict.setText(ci.district);
        contactViewHolder.vType.setText(ci.type);
        contactViewHolder.vContactForm.setText(ci.contactForm);
        contactViewHolder.vContactButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(ci.contactForm));
                v.getContext().startActivity(browserIntent);
            }
        });
        contactViewHolder.vDetailsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailedView.class);
                intent.putExtra("state", ci.state);
                intent.putExtra("district", ci.district);
                intent.putExtra("chamber", ci.type);
                intent.putExtra("id", ci.id);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_layout, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        protected TextView vName;
        protected TextView vParty;
        protected TextView vContactForm;
        protected TextView vDistrict;
        protected TextView vType;
        protected Button vContactButton;
        protected Button vDetailsButton;

        public ContactViewHolder(View v) {
            super(v);
            vName =  (TextView) v.findViewById(R.id.txtName);
            vParty = (TextView) v.findViewById(R.id.partyName);
            vContactForm = (TextView)  v.findViewById(R.id.contactForm);
            vDistrict = (TextView) v.findViewById(R.id.districtName);
            vType = (TextView) v.findViewById(R.id.typeName);
            vContactButton = (Button) v.findViewById(R.id.contactButton);
            vDetailsButton = (Button) v.findViewById(R.id.detailsButton);
        }
    }
}

