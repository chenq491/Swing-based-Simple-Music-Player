package com.chenq.panel;

import com.chenq.player.Player;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

//播放列表的对话框
public class MusicListDialog extends JDialog {

    private JTable table;//显示歌曲名称和时间的表格

    public MusicListDialog(JFrame parent) {
        super(parent, "播放列表", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    //设置播放列表里的内容
    void setList(Player player) {
        Object[][] data = new Object[player.getMusicList().size()][2];
        for (int i = 0; i < player.getMusicList().size(); i++) {
            data[i][0] = player.getMusicList().get(i).getMusicName();
            data[i][1] = player.formatTime(player.getMusicList().get(i).getMusicTime());
        }
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("歌曲名称");
        model.addColumn("歌曲时长");
        for (Object[] row : data) {
            model.addRow(row);
        }
        table = new JTable(model);
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                //设置点击两次歌名播放歌曲
                if (e.getClickCount() == 2 && e.getSource() == table) {
                    int row = table.rowAtPoint(e.getPoint());//获取点击的行数
                    if (row >= 0 && row < table.getRowCount()) {
                        player.setCurrentSongIndex(row);
                        player.playMusic();
                    }
                }
            }
        });
        //设置歌曲名称和歌曲时间的宽度
        table.getColumnModel().getColumn(0).setPreferredWidth(250);
        table.getColumnModel().getColumn(1).setPreferredWidth(50);
        table.setEnabled(false);//设置不可编辑

        //表格加滚轮
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane);
    }
}

