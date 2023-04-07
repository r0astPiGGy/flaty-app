package hcmute.edu.vn.phamdinhquochoa.flatyapp.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import hcmute.edu.vn.phamdinhquochoa.Flatyapp.R;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.Notify;

public class NotifyCard extends LinearLayout {

    TextView tvTitle, tvContent, tvDateMake;
    Notify notify;

    public NotifyCard(Context context) {
        super(context);
    }

    public NotifyCard(Context context, Notify notify) {
        super(context);
        this.notify = notify;
        initControl(context);
    }
    private void initControl(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.notify_card, this);

        tvTitle = findViewById(R.id.tvTitleNotify);
        tvContent = findViewById(R.id.tvContentNotify);
        tvDateMake = findViewById(R.id.tvDateMakeNotify);

        //Set text
        tvTitle.setText(notify.getTitle());
        tvContent.setText(notify.getContent());
        tvDateMake.setText(notify.getDateMake().toString());

    }
}
