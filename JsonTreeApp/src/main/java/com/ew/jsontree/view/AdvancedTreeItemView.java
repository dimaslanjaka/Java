package com.ew.jsontree.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ew.jsontree.R;

/**
 * Created by WYM on 2016/7/30.
 */
public class AdvancedTreeItemView extends LinearLayout {

    public Button btnExpend;
    private LinearLayout rootLayout;
    private TextView tvNodeKey;
    private TextView tvNodeSize;
    private TextView tvColon;
    private TextView tvNodeValue;

    private int lastExpendState;

    private OnClickListener btnClickListener;


    public AdvancedTreeItemView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.layout_json_tree_item_advanced, this, true);
        initView();
    }

    public AdvancedTreeItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void initView() {
        rootLayout = (LinearLayout) getRootView();
        btnExpend = (Button) this.findViewById(R.id.ew_btn_expend);
        tvNodeKey = (TextView) this.findViewById(R.id.ew_tv_node_key);
        tvNodeSize = (TextView) this.findViewById(R.id.ew_tv_node_size);
        tvColon = (TextView) this.findViewById(R.id.ew_tv_colon);
        tvNodeValue = (TextView) this.findViewById(R.id.ew_tv_node_value);

        btnExpend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnClickListener != null) {
                    btnClickListener.onClick(v);
                }
            }
        });
    }

    public void setExpendState(int expendState) {
        this.lastExpendState = expendState;
    }

    public void setBtnClickListener(OnClickListener btnClickListener) {
        this.btnClickListener = btnClickListener;
    }

    public void setData(String key, String size, Object value, String btnText, Boolean isVirtualNode, Boolean isExpend) {
        tvNodeKey.setText(key);
        if (size != null && !size.isEmpty()) {
            tvNodeSize.setText(size);
            tvNodeSize.setVisibility(VISIBLE);
        }
        if (value != null) {
            tvNodeValue.setText(size);
            tvNodeValue.setVisibility(VISIBLE);
            if (value instanceof String) {
                tvNodeValue.setTextColor(0xff008000);
            } else if (value instanceof Integer || value instanceof Double || value instanceof Long) {
                tvNodeValue.setTextColor(0xffff8c30);
            } else if (value instanceof Boolean) {
                tvNodeValue.setTextColor(0xff3883FA);
            }
        }
        if (btnText != null && !btnText.isEmpty()) {
            btnExpend.setText(btnText);
        }
        if (isVirtualNode) {
            tvNodeKey.setTextColor(0xff7F8081);
        }
        View childView = rootLayout.getChildAt(1);
        if (childView != null) {
            childView.setVisibility(isExpend ? VISIBLE : GONE);
        }
    }
}
