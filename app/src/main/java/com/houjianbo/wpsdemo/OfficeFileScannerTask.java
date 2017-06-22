package com.houjianbo.wpsdemo;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ListView;

import com.houjianbo.Adapter.FileListAdapter;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import com.houjianbo.model.fileInfo;

/**
 * 遍历列出所有视频文件
 * Created by liangxy on 2017/5/26.
 */
public class OfficeFileScannerTask extends AsyncTask<Void, Integer, List<fileInfo>> {
    private List<fileInfo> videoInfos = new ArrayList<fileInfo>();
    private Activity context = null;
    private ListView lv;
    private FileListAdapter adapter= null;

    public OfficeFileScannerTask(List<fileInfo> videoInfos, Activity context, ListView lv, FileListAdapter adapter) {
        this.videoInfos = videoInfos;
        this.context = context;
        this.lv = lv;
        this.adapter = adapter;
    }

    @Override
    protected List<fileInfo> doInBackground(Void... params) {
        videoInfos = getVideoFile(videoInfos, Environment.getExternalStorageDirectory());
        videoInfos = filterVideo(videoInfos);
        Log.i("VideoSize", "office文件数：" + videoInfos.size());
        return videoInfos;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(List<fileInfo> videoInfos) {
        super.onPostExecute(videoInfos);
        adapter = new FileListAdapter(context, videoInfos);
        adapter.notifyDataSetChanged();
        lv.setAdapter(adapter);
    }

    /**
     * 获取视频文件
     *
     * @param list
     * @param file
     * @return
     */
    private List<fileInfo> getVideoFile(final List<fileInfo> list, File file) {

        file.listFiles(new FileFilter() {

            @Override
            public boolean accept(File file) {

                String name = file.getName();

                int i = name.indexOf('.');
                if (i != -1) {
                    name = name.substring(i);
                    if (name.equalsIgnoreCase(".ppt")
                            || name.equalsIgnoreCase(".pptx")
                            || name.equalsIgnoreCase(".xls")
                            || name.equalsIgnoreCase(".xlsx")
                            || name.equalsIgnoreCase(".doc")
                            || name.equalsIgnoreCase(".docx")
                            || name.equalsIgnoreCase(".txt")
                           ) {
                        fileInfo video = new fileInfo();
                        file.getUsableSpace();
                        video.setFileName(file.getName());
                        video.setPath(file.getAbsolutePath());

                        video.setState(1);//默认选中
                        Log.i("tga", "name" + video.getPath());
                        list.add(video);
                        return true;
                    }
                    //判断是不是目录
                } else if (file.isDirectory()) {
                    getVideoFile(list, file);
                }
                return false;
            }
        });

        return list;
    }

    /**
     *判断文件是否存在
     *
     * @param videoInfos
     * @return
     */
    private List<fileInfo> filterVideo(List<fileInfo> videoInfos) {
        List<fileInfo> newVideos = new ArrayList<fileInfo>();
        for (fileInfo videoInfo : videoInfos) {
            File f = new File(videoInfo.getPath());
            if (f.exists() && f.isFile() ) {
                newVideos.add(videoInfo);
                Log.i("TAG", "文件大小" + f.length());
            } else {
                Log.i("TAG", "文件不存在");
            }
        }
        return newVideos;
    }

}
