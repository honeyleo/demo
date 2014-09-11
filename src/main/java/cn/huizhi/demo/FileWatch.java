package cn.huizhi.demo;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FileWatch {
	
	public interface FileListener {
		void onEvent();
	}
	Map<WatchKey, Path> keys = new ConcurrentHashMap<WatchKey, Path>();
	
	Map<String, FileListener> fileListeners = new HashMap<String, FileListener>();
	
	private static WatchService watcher = null;

	static {
		try {
			watcher = FileSystems.getDefault().newWatchService(); // 构建文件监控服务
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void register(Path dir) throws IOException { // IOException
															// ,InterruptedException{
		WatchKey key = dir.register(watcher, ENTRY_MODIFY); // 给文件注册监听事件

		Path existing = keys.get(key);
		if (existing == null) {
			System.out.format("register: %s\n", dir);
		} else if (!dir.equals(existing)) {
			System.out.format("update: %s -> %s\n", existing, dir);
		}

		keys.put(key, dir);
	}

	@SuppressWarnings("unchecked")
	static <T> WatchEvent<Path> cast(WatchEvent<?> event) {
		return (WatchEvent<Path>) event;
	}

	public void fileWatch(File file, FileListener listener) throws IOException, InterruptedException {
		fileListeners.put(file.getPath(), listener);
		String dir = file.getParentFile().getPath();
		Path path= Paths.get(dir);
		fileWatch(path);
	}
	public void fileWatch(Path dir) throws IOException, InterruptedException {
		register(dir);// 先注册主文件夹
		while (true) {
			// 等待监视事件发生
			WatchKey key = watcher.take();

			Path path = keys.get(key);
			if (path == null) {
				return;
			}

			for (WatchEvent<?> event : key.pollEvents()) {
				WatchEvent.Kind<?> kind = event.kind();
				if (kind == OVERFLOW) {
					continue;
				}

				// 目录监视事件的上下文是文件名
				WatchEvent<Path> evt = cast(event);
				Path name = evt.context();
				Path child = path.resolve(name);
				System.out.format(
						new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
								.format(new Date()) + "  %s|%s\n", event.kind()
								.name(), child);
				if (event.kind().name().equals("ENTRY_MODIFY")) {
					FileListener fileListener = fileListeners.get(child.toString());
					if(fileListener != null) {
						fileListener.onEvent();
					}
				}
			}

			// 重置 key
			boolean valid = key.reset();
			if (!valid) {
				keys.remove(key);
				if (keys.isEmpty()) {
					return;
				}
			}
		}
	}

	public static void main(String[] args) {
		final FileWatch fileWatch = new FileWatch();

		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					fileWatch.fileWatch(new File("D:\\test2\\1.txt"), new FileListener() {
						
						@Override
						public void onEvent() {
							System.out.println("读取文件。。。。。。");
						}
					});
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					fileWatch.fileWatch(new File("D:\\test2\\2.txt"), new FileListener() {
						
						@Override
						public void onEvent() {
							System.out.println("读取文件2。。。。。。");
						}
					});
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		
	}
}
