/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jagwarez.game.asset.reader;

import java.io.File;
import java.io.FileFilter;

/**
 *
 * @author jacob
 * @param <O>
 */
public class FilesReader implements AssetReader<File> {
    
    private final File[] files;
    private int index;
    
    public FilesReader(File file) {
        this(file, null);
    }
    
    public FilesReader(File file, FileFilter filter) {
        this.files = file.isDirectory() ? file.listFiles(filter) :  new File[] { file };
        this.index = 0;
    }
    
    @Override
    public File read() throws Exception {
        return index < files.length ? files[index++] : null;
    }
}
