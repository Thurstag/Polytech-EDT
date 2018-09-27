package com.polytech.edt.model.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import com.google.common.base.Predicate;
import com.polytech.edt.AppCache;
import com.polytech.edt.R;
import com.polytech.edt.config.UserConfig;
import com.polytech.edt.model.ADEResource;
import com.polytech.edt.model.tree.Node;
import com.polytech.edt.util.LOGGER;
import com.polytech.edt.util.ListUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupListAdapter extends SimpleExpandableListAdapter {

    private static final String[] keys = new String[]{"KEY"};
    private static final int[] groupIds = new int[]{R.id.expandable_list_header};
    private static final int[] childIds = new int[]{R.id.expandable_list_item};

    @SuppressLint("UseSparseArrays")
    private static Map<Integer, Boolean> isSection = new HashMap<>();
    private ColorStateList defaultTextColor = null;
    private CompoundButton.OnCheckedChangeListener checkedChangeListener = null;

    public GroupListAdapter(Context context, List<Node<String, List<ADEResource>>> resources) {
        super(context, buildGroups(resources), R.layout.expandable_list_header, keys, groupIds, buildChildren(resources), R.layout.expandable_list_item, keys, childIds);
    }

    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener checkedChangeListener) {
        this.checkedChangeListener = checkedChangeListener;
    }

    private static List<? extends Map<String, ?>> buildGroups(List<Node<String, List<ADEResource>>> resources) {
        List<Map<String, String>> groups = new ArrayList<>();
        String section = null;

        // Add groups
        for (Node<String, List<ADEResource>> node : resources) {
            if (section == null || !section.equals(node.parent().id())) {
                groups.add(new HashMap<String, String>());
                groups.get(groups.size() - 1).put(keys[0], node.parent().id());

                isSection.put(groups.size() - 1, true);
            }

            groups.add(new HashMap<String, String>());
            groups.get(groups.size() - 1).put(keys[0], node.id());

            // Update section
            section = node.parent().id();
        }

        return groups;
    }

    private static List<? extends List<? extends Map<String, ?>>> buildChildren(List<Node<String, List<ADEResource>>> resources) {
        List<List<Map<String, String>>> children = new ArrayList<>();
        String section = null;

        for (Node<String, List<ADEResource>> node : resources) {
            if (section == null || !section.equals(node.parent().id())) {
                children.add(new ArrayList<Map<String, String>>());
            }

            List<Map<String, String>> child = new ArrayList<>();

            for (ADEResource resource : node.content()) {
                if (resource.name().isEmpty()) {
                    continue;
                }

                child.add(new HashMap<String, String>());
                child.get(child.size() - 1).put(keys[0], resource.name());
            }

            children.add(child);

            // Update section
            section = node.parent().id();
        }

        return children;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = super.getChildView(groupPosition, childPosition, isLastChild, convertView, parent);

        // Attach a listener on change
        final AppCompatCheckBox checkBox = view.findViewById(childIds[0]);
        if (checkBox != null) {
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (checkedChangeListener != null) {
                        checkedChangeListener.onCheckedChanged(buttonView, isChecked);
                    }
                }
            });

            // Check box
            checkBox.setChecked(ListUtil.contains(((UserConfig) AppCache.get("config")).groups(), new Predicate<ADEResource>() {
                @Override
                public boolean apply(@Nullable ADEResource input) {
                    return input != null && input.name() != null && input.name().equals(checkBox.getText().toString());
                }
            }));
        }

        return view;
    }

    @Override
    @SuppressLint("CutPasteId")
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = super.getGroupView(groupPosition, isExpanded, convertView, parent);

        TextView textView = view.findViewById(R.id.expandable_list_header);
        View emptyIndicator = view.findViewById(R.id.group_empty_indicator);
        ImageView down = view.findViewById(R.id.group_arrow_down);
        ImageView up = view.findViewById(R.id.group_arrow_up);

        // Back up default color
        if (defaultTextColor == null) {
            defaultTextColor = textView.getTextColors();
        }

        // Refactor into section style
        if (isSection.containsKey(groupPosition)) {
            emptyIndicator.setVisibility(View.VISIBLE);
            up.setVisibility(View.GONE);
            down.setVisibility(View.GONE);

            view.setBackgroundColor(view.getResources().getColor(R.color.colorPrimary));
            textView.setTextColor(Color.WHITE);
            return view;
        }

        // Display arrow
        if (isExpanded) {
            up.setVisibility(View.VISIBLE);
            down.setVisibility(View.GONE);
        }
        else {
            down.setVisibility(View.VISIBLE);
            up.setVisibility(View.GONE);
        }

        // Set default text color
        textView.setTextColor(defaultTextColor);

        // Hide fake image
        emptyIndicator.setVisibility(View.GONE);

        // Set default background
        view.setBackground(new ColorDrawable());

        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        try {
            return super.getChildrenCount(groupPosition);
        } catch (IndexOutOfBoundsException ignored) {
            return 0;
        } catch (Exception e) {
            LOGGER.error(e);
            return 0;
        }
    }
}
