package com.rosendal.elevkarrosendal.fragments.schedule.id_picker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rosendal.elevkarrosendal.R;

import java.util.ArrayList;

public class IdAdapter extends RecyclerView.Adapter<IdAdapter.ViewHolder> {
    private static final int TYPE_NORMAL = 0, TYPE_ADD = 1;
    public int selectedIndex;

    public ArrayList<String> mData;
    private IdDialog dialog;
    private Context context;

    public IdAdapter(ArrayList<String> data, IdDialog dialog, Context c) {
        mData = data;
        this.dialog = dialog;
        context = c;
    }

    public void setSelected(int pos) {
        if (selectedIndex != pos) {
            int n = selectedIndex;
            selectedIndex = pos;
            notifyItemChanged(n);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        ImageView imageView;
        RelativeLayout rl;

        ViewHolder(View view) {
            super(view);
            text = (TextView) view.findViewById(R.id.tvId);
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int n = mData.size();
                    int i = 0;
                    for (String s : mData) {
                        if (s.equals(text.getText().toString())) {
                            n = i;
                            break;
                        }
                        i++;
                    }
                    setSelected(n);
                    rl.setBackgroundResource(R.color.primary300);
                }
            });
            imageView = (ImageView) view.findViewById(R.id.iwDeleteId);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int n = mData.size();
                    int i = 0;
                    for (String s : mData) {
                        if (s.equals(text.getText().toString())) {
                            n = i;
                            break;
                        }
                        i++;
                    }
                    if (n < mData.size()) {
                        mData.remove(n);
                        notifyItemRemoved(n);
                        if (n <= selectedIndex && selectedIndex > 0) {
                            selectedIndex--;
                            notifyItemChanged(selectedIndex);
                        }
                    }
                }
            });
            rl = (RelativeLayout) view.findViewById(R.id.rlIdItemBG);
        }
    }
    class AddViewHolder extends ViewHolder {
        AddViewHolder(View view) {
            super(view);
            text.setOnClickListener(null);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= 12) {
                        dialog.idSelect.setAlpha(0f);
                        dialog.idSelect.setVisibility(View.VISIBLE);
                        dialog.idSelect.animate()
                                .alpha(1f)
                                .setDuration(dialog.mShortAnimationDuration)
                                .setListener(null);
                        dialog.recyclerView.animate()
                                .alpha(0f)
                                .setDuration(dialog.mShortAnimationDuration)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        dialog.recyclerView.setVisibility(View.GONE);
                                    }
                                });
                    } else {
                        dialog.recyclerView.setVisibility(View.GONE);
                        dialog.idSelect.setVisibility(View.VISIBLE);
                    }
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,0);
                    dialog.text.requestFocus();
                }
            });
        }
    }
    @Override
    public int getItemViewType(int position) {
        if (mData.size() == position + 1) return TYPE_ADD;
        else return TYPE_NORMAL;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.id_item, parent, false);
        if (viewType == TYPE_ADD) {
            return new AddViewHolder(v);
        } else
            return new ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int pos) {
        String item = mData.get(pos);
        if (holder instanceof AddViewHolder) {
            holder.imageView.setImageResource(R.drawable.ic_add_white_36dp);
        } else {
            holder.imageView.setImageResource(R.drawable.ic_clear_white_36dp);
            holder.text.setText(item);
        }
        if (selectedIndex == pos) {
            holder.rl.setBackgroundResource(R.color.primary300);
        } else {
            holder.rl.setBackgroundResource(android.R.color.transparent);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}
