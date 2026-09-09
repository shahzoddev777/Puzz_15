package shahzod.projects.puzzle15;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Collections;


public class GameActivity extends AppCompatActivity {
    private int[][] matrix = new int[4][4];
    private TextView[] views = new TextView[16];
    private SharedPreferences preferences;
    private String name;
    private int x = -1;
    private int y = -1;
    private int count = 0;
    private TextView textCount;
    private AppCompatImageButton btnRestart, btnBack;

    private int lastMovedValue = -1;
    private int previousEmptyIndex = -1;

    private String playerUsername;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);

        name = getIntent().getStringExtra("uernameMain");
        if (name == null || name.isEmpty()) {
            name = "Mehmon";
        }
        playerUsername = name;
        preferences = getSharedPreferences("PUZZLE_SAVE", MODE_PRIVATE);
        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        textCount = findViewById(R.id.counttxt);
        btnRestart = findViewById(R.id.res);
        btnBack = findViewById(R.id.orqaga);
        loadViews();
        clickUIViews();
        loadMatrix();
        btnBack.setOnClickListener(view -> finish());
        btnRestart.setOnClickListener(view -> showRestartDialog());
    }

    private void showRestartDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_custom, null);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        TextView message = view.findViewById(R.id.dialog_message);
        message.setText("O'yinni qaytadan boshlashni xohlaysizmi?");

        AppCompatButton btnNo = view.findViewById(R.id.btn_no);
        AppCompatButton btnYes = view.findViewById(R.id.btn_yes);

        btnNo.setOnClickListener(v -> dialog.dismiss());
        btnYes.setOnClickListener(v -> {
            dialog.dismiss();
            count = 0;
            textCount.setText("0");
            lastMovedValue = -1;
            previousEmptyIndex = -1;
            loadMatrix();
            Toast.makeText(GameActivity.this, "Restart berildi!", Toast.LENGTH_SHORT).show();
        });

        dialog.show();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("count_key", count);
        String[] mass = new String[16];
        for (int i = 0; i < 16; i++) {
            if (views[i] != null) {
                mass[i] = views[i].getText().toString();
            } else {
                mass[i] = "16";
            }
        }
        outState.putStringArray("massiv", mass);
        outState.putInt("xbutton", x);
        outState.putInt("ybutton", y);
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        count = savedInstanceState.getInt("count_key");
        textCount.setText(String.valueOf(count));
        y = savedInstanceState.getInt("ybutton");
        x = savedInstanceState.getInt("xbutton");
        String[] mas = savedInstanceState.getStringArray("massiv");
        if (mas != null) {
            for (int i = 0; i < Math.min(mas.length, 16); i++) {
                if (views[i] == null) continue;
                if ("16".equals(mas[i])) {
                    views[i].setText(mas[i]);
                    views[i].setVisibility(View.INVISIBLE);
                } else {
                    views[i].setText(mas[i]);
                    views[i].setVisibility(View.VISIBLE);
                }
            }
        }

    }

    private void loadViews() {
        RelativeLayout relativeLayout = findViewById(R.id.relativeLayout);
        if (relativeLayout == null) return;
        int childCount = Math.min(relativeLayout.getChildCount(), 16);
        for (int i = 0; i < childCount; i++) {
            View child = relativeLayout.getChildAt(i);
            if (child instanceof TextView) {
                views[i] = (TextView) child;
            }
        }
    }


    @Override
    protected void onPause() {
        if (name == null) name = "Mehmon";
        preferences.edit()
                .putInt("count", count)
                .putString("name", name)
                .putBoolean("saved", true)
                .apply();

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            if (views[i] != null) {
                builder.append(views[i].getText().toString()).append('#');
            } else {
                builder.append("16#");
            }
        }
        preferences.edit().putString("massiv", builder.toString()).apply();
        if (isFinish()) {
            preferences.edit().clear().apply();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (preferences.getBoolean("saved", false)) {
            count = preferences.getInt("count", 0);
            textCount.setText(String.valueOf(count));
            name = preferences.getString("name", "Mehmon");
            String massivStr = preferences.getString("massiv", "");
            if (!massivStr.isEmpty() && !massivStr.equals("non")) {
                String[] str = massivStr.split("#");
                for (int i = 0; i < Math.min(str.length, 16); i++) {
                    if (views[i] == null) continue;
                    views[i].setText(str[i]);
                    if (str[i].equals("16")) {
                        views[i].setVisibility(View.INVISIBLE);
                        x = i / 4;
                        y = i % 4;
                    } else {
                        views[i].setVisibility(View.VISIBLE);
                    }
                }
            }
        }
        super.onResume();
    }

    private void clickUIViews() {
        for (int i = 0; i < views.length; i++) {
            if (views[i] == null) continue;
            views[i].setTag(i);
            views[i].setOnClickListener(view -> {
                Object tag = view.getTag();
                if (tag == null) return;
                int amount = (int) tag;
                if (canMove(amount)) {
                    int currentEmptyIndex = x * 4 + y;
                    int movedValue = Integer.parseInt(views[amount].getText().toString());

                    if (movedValue == lastMovedValue && amount == previousEmptyIndex) {
                        lastMovedValue = -1;
                        previousEmptyIndex = -1;
                    } else {
                        count++;
                        lastMovedValue = movedValue;
                        previousEmptyIndex = currentEmptyIndex;
                    }

                    swapAmount(amount);
                    textCount.setText(String.valueOf(count));

                    MediaPlayer mediaPlayer = MediaPlayer.create(GameActivity.this, R.raw.music);
                    if (mediaPlayer != null) {
                        mediaPlayer.start();
                        mediaPlayer.setOnCompletionListener(MediaPlayer::release);
                    }
                    if (isFinish()) {
                        Intent intent = new Intent(GameActivity.this, RecordActivity.class);
                        intent.putExtra("SCORE_KEY", count);
                        intent.putExtra("NAME_KEY", name);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }

    private void loadMatrix() {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 1; i <= 16; i++) list.add(i);
        do {
            Collections.shuffle(list);
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    matrix[i][j] = list.get(4 * i + j);
                }
            }
        } while (!isSolvable(matrix));

        showAmountToViews();
    }

    private void showAmountToViews() {
        for (int i = 0; i < views.length; i++) {
            int value = matrix[i / 4][i % 4];
            views[i].setText(String.valueOf(value));
            if (value == 16) {
                views[i].setVisibility(View.INVISIBLE);
                x = i / 4;
                y = i % 4;
            } else {
                views[i].setVisibility(View.VISIBLE);
            }
        }
    }

    private boolean canMove(int index) {
        int xPos = index / 4;
        int yPos = index % 4;
        return (Math.abs(xPos - x) == 1 && yPos == y) || (Math.abs(yPos - y) == 1 && xPos == x);
    }

    private void swapAmount(int index) {
        int z = x * 4 + y;
        views[z].setText(views[index].getText());
        views[index].setText("16");
        views[z].setVisibility(View.VISIBLE);
        views[index].setVisibility(View.INVISIBLE);
        x = index / 4;
        y = index % 4;
    }

    private boolean isFinish() {
        if (!(x == 3 && y == 3)) return false;
        for (int i = 0; i < 15; i++) {
            if (views[i] == null || !views[i].getText().toString().equals(String.valueOf(i + 1)))
                return false;
        }
        return true;
    }

    private boolean isSolvable(int[][] puzzle) {
        int[] arr = new int[16];
        int k = 0;
        int emptyRowFromBottom = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                arr[k++] = puzzle[i][j];
                if (puzzle[i][j] == 16) emptyRowFromBottom = 4 - i;
            }
        }
        int invCount = 0;
        for (int i = 0; i < 15; i++) {
            for (int j = i + 1; j < 16; j++) {
                if (arr[i] != 16 && arr[j] != 16 && arr[i] > arr[j]) invCount++;
            }
        }
        return (emptyRowFromBottom % 2 == 1) ? (invCount % 2 == 0) : (invCount % 2 == 1);
    }
}
