package gui;

public class RenderThread extends Thread{

	private GameGui gg;
	
	private int maxFps;
	
	private int currentFps;
	
	private boolean showFps = false;
	
	public RenderThread(GameGui gg, int maxFps) {
		this.gg = gg;
		this.maxFps = maxFps;
		currentFps = 0;
	}

	@Override
	public void run(){
		long t0 = System.currentTimeMillis();
		double intervallTime = 1000/(double)maxFps;
		long fpsCounterTimer = System.currentTimeMillis();
		int fpsCounter = 0;
		while(true){
			t0 = System.currentTimeMillis();
			gg.render();
			fpsCounter++;
			if (System.currentTimeMillis()-fpsCounterTimer >= 1000){
				if (showFps)
					System.out.println("fps: " + fpsCounter);
				currentFps = fpsCounter;
				fpsCounter = 0;
				fpsCounterTimer = System.currentTimeMillis();
			}
			
			long sleepingTime = (long)(intervallTime - (System.currentTimeMillis() - t0));
			if (sleepingTime < 0){
				//rendering took longer than fpsIntervall -> fps is smaller than maxFps
				
			}
			else{
				try {
					Thread.sleep(sleepingTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}

	public boolean isShowFps() {
		return showFps;
	}

	public void setShowFps(boolean showFps) {
		this.showFps = showFps;
	}
	
	
}
