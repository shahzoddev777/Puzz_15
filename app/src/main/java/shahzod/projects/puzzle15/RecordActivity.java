package shahzod.projects.puzzle15;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RecordActivity extends AppCompatActivity {
    Button home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);

        TextView tvCurrent = findViewById(R.id.current_score_tv);
        TextView tvTop1 = findViewById(R.id.top1_tv);
        TextView tvTop2 = findViewById(R.id.top2_tv);
        TextView tvTop3 = findViewById(R.id.top3_tv);
        home = findViewById(R.id.homebtn);

        int currentScore = getIntent().getIntExtra("SCORE_KEY", 0);
        String currentName = getIntent().getStringExtra("NAME_KEY");
        if (currentName == null || currentName.isEmpty()) currentName = "Mehmon";

        if (tvCurrent != null) {
            tvCurrent.setText(currentName + " natijangiz: " + currentScore);
        }

        SharedPreferences recordPrefs = getSharedPreferences("TopScores", MODE_PRIVATE);
        String name1 = recordPrefs.getString("top1_name", "---");
        String name2 = recordPrefs.getString("top2_name", "---");
        String name3 = recordPrefs.getString("top3_name", "---");

        int count1 = recordPrefs.getInt("top1_count", 99999);
        int count2 = recordPrefs.getInt("top2_count", 99999);
        int count3 = recordPrefs.getInt("top3_count", 99999);

        boolean isUpdated = false;

        if (currentScore < count1) {
            count3 = count2; name3 = name2;
            count2 = count1; name2 = name1;
            count1 = currentScore; name1 = currentName;
            isUpdated = true;
        } else if (currentScore < count2) {
            count3 = count2; name3 = name2;
            count2 = currentScore; name2 = currentName;
            isUpdated = true;
        } else if (currentScore < count3) {
            count3 = currentScore; name3 = currentName;
            isUpdated = true;
        }

        if (isUpdated) {
            recordPrefs.edit()
                .putString("top1_name", name1)
                .putInt("top1_count", count1)
                .putString("top2_name", name2)
                .putInt("top2_count", count2)
                .putString("top3_name", name3)
                .putInt("top3_count", count3)
                .apply();
        }

        if (tvTop1 != null) tvTop1.setText("1. " + name1 + " : " + (count1 == 99999 ? 0 : count1));
        if (tvTop2 != null) tvTop2.setText("2. " + name2 + " : " + (count2 == 99999 ? 0 : count2));
        if (tvTop3 != null) tvTop3.setText("3. " + name3 + " : " + (count3 == 99999 ? 0 : count3));

        if (home != null) {
            home.setOnClickListener(view -> finish());
        }
    }
}