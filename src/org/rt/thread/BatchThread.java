/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rt.thread;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author user
 * @param <K>
 */
public class BatchThread <K extends KernelThread>
{
    ArrayList<K> threadList;
    
    public BatchThread()
    {
        threadList = new ArrayList<>();
    }
    
    public BatchThread(K... threads)            
    {
        this();
        threadList.addAll(Arrays.asList(threads));
    }
    
    public K get(int index)
    {
        if(!threadList.isEmpty())
            return threadList.get(index);
        else
            return null;
    }
    
    public void add(K... threads)
    {
        threadList.addAll(Arrays.asList(threads));
    }
    
    public void start()
    {
        for(KernelThread thread : threadList)
            thread.start();
    }
    
    public void pause()
    {
        for(KernelThread thread : threadList)
            thread.pause();
    }
    
    public void resume()
    {
        for(KernelThread thread : threadList)
            thread.resume();
    }
    
    public void stop()
    {
        for(KernelThread thread : threadList)            
            thread.stop();
        
        threadList.removeAll(threadList);
    }
    
    public void join()
    {
        for(KernelThread thread : threadList)
            thread.join();
    }
    
    public boolean isThreadListEmpty()
    {
        return threadList.isEmpty();
    }
    
    public static int getCoreThreadNumber()
    {
        return Runtime.getRuntime().availableProcessors();
    }
}
