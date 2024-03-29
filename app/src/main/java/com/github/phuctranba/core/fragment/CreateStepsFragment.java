package com.github.phuctranba.core.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.github.phuctranba.core.adapter.CreateIngredientAdapter;
import com.github.phuctranba.core.adapter.CreateStepAdapter;
import com.github.phuctranba.core.item.ItemIngredient;
import com.github.phuctranba.sharedkitchen.CreateRecipeActivity;
import com.github.phuctranba.sharedkitchen.R;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreateStepsFragment extends Fragment {

    ListView listViewIngredients, listViewStep;
    static List<String> listStep;
    static List<ItemIngredient> listIngredient;
    CreateIngredientAdapter createIngredientAdapter;
    CreateStepAdapter createStepAdapter;
    ImageView addStep, addIngredient;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_create_step, container, false);
        setHasOptionsMenu(true);

        Init(rootView);

        setClick();

        return rootView;
    }

    //Ánh xạ các thành phần
    void Init(View rootview) {
        listViewIngredients = rootview.findViewById(R.id.listview_ingredients);
        listViewStep = rootview.findViewById(R.id.listview_step);
        addStep = rootview.findViewById(R.id.addStep);
        addIngredient = rootview.findViewById(R.id.addIngredient);

        listIngredient = new ArrayList<>();

        createIngredientAdapter = new CreateIngredientAdapter(getActivity(), listIngredient);
        listViewIngredients.setAdapter(createIngredientAdapter);

        listStep = new ArrayList<>();

        createStepAdapter = new CreateStepAdapter(getActivity(), listStep);
        listViewStep.setAdapter(createStepAdapter);
    }

    //Cài đặt click
    void setClick() {

        //Khi click vào 1 dòng nguyên liệu
        listViewIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayEditIngredientDialog(position,false);
            }
        });

        //Khi click vào 1 dòng bước làm
        listViewStep.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayEditStepDialog(position,false);
            }
        });

        //Khi click nút thêm nguyên liệu
        addIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayEditIngredientDialog(0,true);
            }
        });

        //Khi click nút thêm bước làm
        addStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayEditStepDialog(0,true);
            }
        });
    }

    //hiển thị dialog chỉnh sủa hoặc thêm nguyên liệu
    public void displayEditIngredientDialog(int position, boolean isnew) {
        final LayoutInflater inflater = getLayoutInflater();
        final View dialogLayout = inflater.inflate(R.layout.dialog_create_ingredient, null);
        final EditText editName = (EditText) dialogLayout.findViewById(R.id.name_ingredient);
        final EditText editAmount = (EditText) dialogLayout.findViewById(R.id.amount_ingredient);

        ItemIngredient itemIngredient;

        if (isnew) {
            itemIngredient = new ItemIngredient();
            itemIngredient.setIngredientId(UUID.randomUUID().toString());
        }else {
            itemIngredient = listIngredient.get(position);
            editName.setText(itemIngredient.getIngredientName());
            editAmount.setText(itemIngredient.getIngredientAmount());
        }

        final AlertDialog.Builder editDialog = new AlertDialog.Builder(getActivity());
        editDialog.setView(dialogLayout);
        editDialog.setCancelable(false);
        editDialog.setNegativeButton("Hủy", null);

        if (!isnew) {
            editDialog.setNeutralButton("Xóa", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listIngredient.remove(position);
                    createIngredientAdapter.notifyDataSetChanged();
                }
            });
        }

        editDialog.setPositiveButton("Lưu", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = editName.getText().toString().trim();
                String amount = editAmount.getText().toString().trim();

                if(name.equals("")){
                    Toast.makeText(getActivity(),"Tên nguyên liệu không được trống",Toast.LENGTH_SHORT).show();
                    return;
                }

                itemIngredient.setIngredientName(name);
                itemIngredient.setIngredientAmount(amount);
                if (isnew) {
                    listIngredient.add(itemIngredient);
                }else {
                    listIngredient.set(position, itemIngredient);
                }

                createIngredientAdapter.notifyDataSetChanged();
            }

        });
        AlertDialog dialog = editDialog.create();
        dialog.show();
    }

    //Hiển thị dialog chỉnh sửa hoặc thêm bước
    public void displayEditStepDialog(int position, boolean isnew) {
        final LayoutInflater inflater = getLayoutInflater();
        final View dialogLayout = inflater.inflate(R.layout.dialog_create_step, null);
        final EditText editTextStep = (EditText) dialogLayout.findViewById(R.id.step);

        if (!isnew) {
            editTextStep.setText(listStep.get(position));
        }

        final AlertDialog.Builder editDialog = new AlertDialog.Builder(getActivity());
        editDialog.setView(dialogLayout);
        editDialog.setCancelable(false);
        editDialog.setNegativeButton("Hủy", null);

        if (!isnew) {
            editDialog.setNeutralButton("Xóa", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listStep.remove(position);
                    createStepAdapter.notifyDataSetChanged();
                }
            });
        }


        editDialog.setPositiveButton("Lưu", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String step = editTextStep.getText().toString().trim();
                if(step.equals("")){
                    Toast.makeText(getActivity(),"Nội dung không được trống",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isnew) {
                    listStep.add(step);
                }else {
                    listStep.set(position, step);
                }

                createStepAdapter.notifyDataSetChanged();
            }

        });
        AlertDialog dialog = editDialog.create();
        dialog.show();
    }

    //Lưu các bước và nguyên liệu vào công thức
    public static void setData() {
        CreateRecipeActivity.recipe.setRecipeSteps(listStep);
        CreateRecipeActivity.recipe.setRecipeIngredient(listIngredient);
    }
}
