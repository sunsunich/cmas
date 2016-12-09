package org.cmas.activities.file;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import org.cmas.mobile.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * User: ABadretdinov
 * Date: 05.08.13
 * Time: 13:33
 */
public class FileChooseAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<File> mFiles = new ArrayList<File>();
    private boolean isSd = true;

    public FileChooseAdapter(Context context, File file, FileChooseActivity.Mode mode, String type) {
        File[] files = file.listFiles();
        if (files != null) {
            Arrays.sort(files);
        }
        if (!file.getAbsolutePath().equals(Environment.getExternalStorageDirectory().getAbsolutePath())) {
            isSd = false;
            mFiles.add(file.getParentFile());
        }
        if (files != null) {
            for (File innerFile : files) {
                /*
                * если нужно выбрать директорию, то только папки добавляем в список.
                * если нужно выбрать файл, то добавляем папки и файлы, при чем файл может иметь тип type
                * */
                if ((mode != FileChooseActivity.Mode.CHOOSE_FILE && innerFile.isDirectory() && innerFile.canWrite())
                        || (mode == FileChooseActivity.Mode.CHOOSE_FILE && (innerFile.isDirectory() || TextUtils.isEmpty(type) || innerFile.getName().endsWith(type)))) {
                    mFiles.add(innerFile);
                }
            }
        }
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mFiles.size();
    }

    @Override
    //первым элементом идет кнопка возврата на директорию выше. нужно обработать, в каком случае она нужна, а в каком нет
    public File getItem(int position) {
        return mFiles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class FileHolder {
        TextView name;
        TextView hint;
    }

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        FileHolder holder;
        if (convertView == null) {
            vi = mInflater.inflate(R.layout.file_row, null);
            holder = new FileHolder();
            holder.name = (TextView) vi.findViewById(android.R.id.text1);
            holder.hint = (TextView) vi.findViewById(android.R.id.text2);
            vi.setTag(holder);
        } else {
            holder = (FileHolder) vi.getTag();
        }
        File file = getItem(position);
        if (position == 0 && !isSd) {
            holder.name.setText("..");
            holder.hint.setText(mInflater.getContext().getString(R.string.cv_parent_directory));
        } else {
            holder.name.setText(file.getName());
            holder.hint.setText(dateFormat.format(new Date(file.lastModified())));
        }
        return vi;
    }
}

