package com.sddd.tfn.myijkplayer;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;


/**
 * 文件列表Fragment界面
 */
public class FileListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private FileClickListener mListener = null;
    private static final String ARG_DIR_PATH = "dir_path";
    private String mDirPath = null;
    private EditText mPathView = null;
    private ListView mFileListView = null;
    private FileListAdapter mAdapter = null;

    public FileListFragment() {
        // Required empty public constructor
    }

    /**
     * 产生FileListFragment实例
     *
     * @param dirPath 文件夹路径.
     * @return FileListFragment实例.
     */
    public static FileListFragment newInstance(String dirPath) {
        FileListFragment fragment = new FileListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DIR_PATH, dirPath);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FileClickListener) {
            mListener = (FileClickListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement FileClickListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.fragment_file_list, container, false);

        mPathView = (EditText) vg.findViewById(R.id.path_view);
        mFileListView = (ListView) vg.findViewById(R.id.file_list_view);

        return vg;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            mDirPath = getArguments().getString(ARG_DIR_PATH);
            mDirPath = new File(mDirPath).getAbsolutePath();
            mPathView.setText(mDirPath);
        }

        Activity activity = getActivity();
        mAdapter = new FileListAdapter(activity);
        mFileListView.setAdapter(mAdapter);
        mFileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {
                String path = mAdapter.getFilePath(position);
                if (TextUtils.isEmpty(path)) {
                    return;
                }

                //点击处理
                //回调给Activity处理
                if (null != mListener) {
                    mListener.onFileClick(path);
                }

            }
        });

        getLoaderManager().initLoader(1, null, this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (null != mListener) {
            mListener = null;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (TextUtils.isEmpty(mDirPath)) {
            return null;
        }
        return new PathCursorLoader(getActivity(), mDirPath);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (null != mAdapter) {
            mAdapter.swapCursor(data);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    class FileListAdapter extends SimpleCursorAdapter {
        public FileListAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_2, null,
                    new String[]{PathCursor.CN_FILE_NAME, PathCursor.CN_FILE_PATH},
                    new int[]{android.R.id.text1, android.R.id.text2}, 0);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (null == view) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                view = inflater.inflate(R.layout.fragment_file_list_item, parent, false);
            }

            ViewHolder viewHolder = (ViewHolder) view.getTag();
            if (null == viewHolder) {
                viewHolder = new ViewHolder();
                viewHolder.iconImageView = (ImageView) view.findViewById(R.id.icon);
                viewHolder.nameTextView = (TextView) view.findViewById(R.id.name);
            }

            if (isDirectory(position)) {
                viewHolder.iconImageView.setImageResource(R.mipmap.ic_theme_folder);
            } else if (isVideo(position)) {
                viewHolder.iconImageView.setImageResource(R.mipmap.ic_theme_play_arrow);
            } else {
                viewHolder.iconImageView.setImageResource(R.mipmap.ic_theme_description);
            }
            viewHolder.nameTextView.setText(getFileName(position));

            return view;
        }

        @Override
        public long getItemId(int position) {
            final Cursor cursor = moveToPosition(position);
            if (cursor == null)
                return 0;

            return cursor.getLong(PathCursor.CI_ID);
        }

        boolean isDirectory(int position) {
            final Cursor cursor = moveToPosition(position);
            if (cursor == null)
                return true;

            return cursor.getInt(PathCursor.CI_IS_DIRECTORY) != 0;
        }

        boolean isVideo(int position) {
            final Cursor cursor = moveToPosition(position);
            if (cursor == null)
                return true;

            return cursor.getInt(PathCursor.CI_IS_VIDEO) != 0;
        }

        String getFileName(int position) {
            final Cursor cursor = moveToPosition(position);
            if (cursor == null)
                return "";

            return cursor.getString(PathCursor.CI_FILE_NAME);
        }

        public String getFilePath(int position) {
            final Cursor cursor = moveToPosition(position);
            if (cursor == null)
                return "";

            return cursor.getString(PathCursor.CI_FILE_PATH);
        }

        Cursor moveToPosition(int position) {
            final Cursor cursor = getCursor();
            if (cursor.getCount() == 0 || position >= cursor.getCount()) {
                return null;
            }
            cursor.moveToPosition(position);
            return cursor;
        }

        class ViewHolder {
            ImageView iconImageView;
            TextView nameTextView;
        }
    }

}
