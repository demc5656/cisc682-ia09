package com.example.helloworld;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helloworld.adapters.ToolAdapter;
import com.example.helloworld.interfaces.ToolListener;
import com.example.helloworld.models.ToolItem;
import com.example.helloworld.widgets.PaintView;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import top.defaults.view.PickerView;

public class MainActivity extends AppCompatActivity implements ToolListener {

    PaintView mPaintView;
    int backgroundColor, brushColor;
    int brushSize;
    int opacity;
    String brushShape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initTools();
    }

    private void initTools() {
        backgroundColor = Color.WHITE;
        brushColor = Color.BLACK;
        brushSize = 10;
        brushShape = "ROUND";
        mPaintView = findViewById(R.id.painter);
        RecyclerView recyclerView = findViewById(R.id.tool_recycler);   // 14:46
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        ToolAdapter toolAdapter = new ToolAdapter(loadTools(), this);
        recyclerView.setAdapter(toolAdapter);
    }

    private List<ToolItem> loadTools() {
        List<ToolItem> result = new ArrayList<>();
        result.add(new ToolItem(R.drawable.baseline_brush_24,"BRUSH"));
        result.add(new ToolItem(R.drawable.baseline_border_color_24,"SIZE"));
        result.add(new ToolItem(R.drawable.baseline_color_lens_24,"COLOR"));
        //result.add(new ToolItem(R.drawable.baseline_blur_linear_24,"OPACITY"));
        result.add(new ToolItem(R.drawable.baseline_delete_24,"CLEAR"));
        return result;
    }

    @Override
    public void onSelect(String name) {
        switch (name) {
            case "BRUSH":
                changeShape();
                break;
            case "SIZE":
                showSizeSlider();
                break;
            case "COLOR":
                changeColor();
                break;
            case "OPACITY":
                showOpacitySlider();
                break;
            case "CLEAR":
                clearCanvas();
                break;
        }
    }

    private void changeShape() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = LayoutInflater.from(this).inflate(R.layout.brushes,null,false);
        TextView currentTool = v.findViewById(R.id.current_tool);
        TextView currentShape = v.findViewById(R.id.shape_status);
        ImageView bShp = v.findViewById(R.id.brush_shape);
        currentTool.setText("Brush Shape");
        bShp.setImageResource(R.drawable.black_brush);
        currentShape.setText("Current Shape: "+brushShape);
        ImageButton roundButt = v.findViewById(R.id.round);
        ImageButton squareButt = v.findViewById(R.id.boxy);
        roundButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brushShape = "ROUND";
                currentShape.setText("Current Shape: "+brushShape);
                mPaintView.setBrushShape(brushShape);
            }
        });
        squareButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brushShape = "SQUARE";
                currentShape.setText("Current Shape: "+brushShape);
                mPaintView.setBrushShape(brushShape);
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setView(v);
        builder.show();
    }

    private void clearCanvas() {
        mPaintView.clearActions();
    }

    private void changeColor() {
        int color = brushColor;
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Select Color")
                .initialColor(color)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setPositiveButton("OK", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, Integer[] integers) {
                        brushColor = i;
                        mPaintView.setBrushColor(brushColor);
                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).build()
                .show();
    }

    private void showOpacitySlider() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = LayoutInflater.from(this).inflate(R.layout.menu,null,false);
        TextView currentTool = v.findViewById(R.id.current_tool);
        TextView currentSize = v.findViewById(R.id.size_status);
        ImageView menuTool = v.findViewById(R.id.menu_tool);
        SeekBar slider = v.findViewById(R.id.brush_size_slider);
        slider.setMax(255);
        currentTool.setText("Opacity");
        menuTool.setImageResource(R.drawable.black_blur);
        currentSize.setText("Current Opacity: "+opacity);
        slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                opacity = progress+1;
                currentSize.setText("Current Opacity: "+opacity);
                mPaintView.setBrushOpacity(opacity);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setView(v);
        builder.show();
    }

    public void showSizeSlider() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = LayoutInflater.from(this).inflate(R.layout.menu,null,false);
        TextView currentTool = v.findViewById(R.id.current_tool);
        TextView currentSize = v.findViewById(R.id.size_status);
        ImageView menuTool = v.findViewById(R.id.menu_tool);
        SeekBar slider = v.findViewById(R.id.brush_size_slider);
        slider.setMax(99);
        currentTool.setText("Brush Size");
        menuTool.setImageResource(R.drawable.black_brush);
        currentSize.setText("Current Size: "+brushSize);
        slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                brushSize = progress+1;
                currentSize.setText("Current Size: "+brushSize);
                mPaintView.setBrushSize(brushSize);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setView(v);
        builder.show();
    }
}