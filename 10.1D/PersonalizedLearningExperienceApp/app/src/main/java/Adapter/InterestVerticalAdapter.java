package Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personalizedlearningexperienceapp.R;

import java.util.ArrayList;
import java.util.List;
import Class.*;

public class InterestVerticalAdapter extends RecyclerView.Adapter<InterestVerticalAdapter.ViewHolder> {
    private List<Interest> data;
    private ItemClickListener listener;
    private List<Interest> returnData;
    public InterestVerticalAdapter(List<Interest> data, ItemClickListener listener) {
        this.data = data;
        this.listener = listener;
        this.returnData = new ArrayList<Interest>();
    }

    public List<Interest> getReturnData() {
        return this.returnData;
    }

    class InterestPairs {
        private Interest interest1;
        private Interest interest2;
        public InterestPairs (Interest interest1, Interest interest2) {
            this.interest1 = interest1;
            this.interest2 = interest2;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ToggleButton Topic1, Topic2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Topic1 = (ToggleButton) itemView.findViewById(R.id.TopicButton1);
            Topic2 = (ToggleButton) itemView.findViewById(R.id.TopicButton2);
        }

        public void InterestBind(InterestPairs interestPairs) {
            // Bind Topic 1
            bindToggleButton(Topic1, interestPairs.interest1);

            // Bind Topic 2
            bindToggleButton(Topic2, interestPairs.interest2);
        }

        private void bindToggleButton(ToggleButton toggleButton, Interest interest) {
            if (interest != null) {
                toggleButton.setText(interest.getTopic());
                toggleButton.setTextOff(interest.getTopic());
                toggleButton.setTextOn(interest.getTopic());
                toggleButton.setVisibility(View.VISIBLE);
                toggleButton.setChecked(returnData.contains(interest));
                toggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        if (returnData.size() < 10) {
                            returnData.add(interest);
                        } else {
                            // User feedback example
                            Toast.makeText(buttonView.getContext(), "You can select up to 10 interests only.", Toast.LENGTH_SHORT).show();
                            toggleButton.setChecked(false);
                        }
                    } else {
                        returnData.remove(interest);
                    }
                });
            } else {
                toggleButton.setVisibility(View.INVISIBLE);
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.interest_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int index1 = position * 2;
        int index2 = index1 + 1;
        Interest interest1 = data.get(index1);
        Interest interest2 = index2 < data.size() ? data.get(index2) : null;
        InterestPairs interestPairs = new InterestPairs(interest1, interest2);
        holder.InterestBind(interestPairs);
    }

    @Override
    public int getItemCount() {
        return (data.size() + 1) / 2;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
