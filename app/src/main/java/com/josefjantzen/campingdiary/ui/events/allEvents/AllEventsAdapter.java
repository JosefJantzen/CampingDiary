package com.josefjantzen.campingdiary.ui.events.allEvents;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.card.MaterialCardView;
import com.josefjantzen.campingdiary.R;
import com.josefjantzen.campingdiary.data.relations.FullReise;
import com.josefjantzen.campingdiary.ui.events.bottomSheet.EventModalBottomSheet;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class AllEventsAdapter  extends RecyclerView.Adapter<AllEventsAdapter.ViewHolder>{

    private static LiveData<FullReise> mDataset;
    private static Context mContext;
    private final FragmentManager mFragmentManager;

    public AllEventsAdapter(LiveData<FullReise> dataset, Context c, FragmentManager fragmentManager) {
        mDataset = dataset;
        mContext = c;
        mFragmentManager = fragmentManager;
    }

    @NonNull
    @NotNull
    @Override
    public AllEventsAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item, parent, false);
        return new AllEventsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AllEventsAdapter.ViewHolder holder, int position) {
        holder.mName.setText(mDataset.getValue().mEvents.get(position).mEvent.getName());
        holder.mDate.setText(mDataset.getValue().mEvents.get(position).mEvent.getDates());
        holder.mCard.setOnClickListener(v -> {
            AllEventsFragmentDirections.ActionAllEventsFragmentToShowEventFragment action = AllEventsFragmentDirections.actionAllEventsFragmentToShowEventFragment();
            action.setReiseId(mDataset.getValue().mReise.getId());
            action.setEventId(mDataset.getValue().mEvents.get(position).mEvent.getId());
            Navigation.findNavController((Activity) mContext, R.id.nav_host).navigate(action);
        });

        holder.mCard.setOnLongClickListener(v -> {
            AllEventsFragmentDirections.ActionAllEventsFragmentToShowEventFragment action = AllEventsFragmentDirections.actionAllEventsFragmentToShowEventFragment();
            action.setReiseId(Objects.requireNonNull(mDataset.getValue()).mReise.getId());
            action.setEventId(mDataset.getValue().mEvents.get(position).mEvent.getId());

            BottomSheetDialogFragment bottomSheet = new EventModalBottomSheet(Objects.requireNonNull(mDataset.getValue()).mEvents.get(position).mPlace, mDataset.getValue().mEvents.get(position).mEvent.getId(), action, mContext);
            bottomSheet.show(mFragmentManager, "EventModalBottomSheet");
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(mDataset.getValue()).mEvents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final MaterialCardView mCard;
        private final TextView mName;
        private final TextView mDate;
        private final ImageView mIcon;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            mCard = itemView.findViewById(R.id.card);
            mName = itemView.findViewById(R.id.tx_name);
            mDate = itemView.findViewById(R.id.tx_date);
            mIcon = itemView.findViewById(R.id.im_cat);
        }
    }
}
