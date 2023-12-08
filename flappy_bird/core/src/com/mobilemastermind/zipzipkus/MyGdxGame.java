package com.mobilemastermind.zipzipkus;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;
//MyGdxGame

public class MyGdxGame extends ApplicationAdapter {


	SpriteBatch batch;



	Texture background;
	Texture bird;
	Texture bee1;
	Texture bee2;
	Texture bee3;
	float birdX = 0;
	float birdY = 0;
	float birdWidth = 0;
	float birdHeight = 0;
	int gameStarted = 0;
	float velocity = 0;
	float gravity = 0.6f;
	float enemyVelocity = 10 ;
	Random random;
	int score =0;
	int scoreEnemy =0;
	BitmapFont font;
	BitmapFont gameOverFont;

	Circle birdCircle;

	ShapeRenderer shapeRenderer;


	int numberOfEnemies = 4;
	float [] enemyX = new float[numberOfEnemies];
	float [] enemyOffSet = new float[numberOfEnemies];
	float [] enemyOffSet2 = new float[numberOfEnemies];
	float [] enemyOffSet3 = new float[numberOfEnemies];

	Circle[] enemyCircles;
	Circle[] enemyCircles2;
	Circle[] enemyCircles3;

	float distance = 0; // uzaklık

	@Override
	public void create () {

		batch = new SpriteBatch();
		background = new Texture("background.png");
		bird = new Texture("bird.png");
		bee1 = new Texture("bee.png");
		bee2 = new Texture("bee.png");
		bee3 = new Texture("bee.png");

		distance = Gdx.graphics.getWidth()/2;
		random = new Random();

		// Kuşun x ve y düzlemindeki konumları
		birdX =Gdx.graphics.getWidth()/4;
		birdY =Gdx.graphics.getHeight()/2;
		birdWidth = Gdx.graphics.getWidth()/16;
		birdHeight = Gdx.graphics.getHeight()/10;

		shapeRenderer = new ShapeRenderer();

		birdCircle = new Circle();
		enemyCircles = new Circle[numberOfEnemies];
		enemyCircles2 = new Circle[numberOfEnemies];
		enemyCircles3 = new Circle[numberOfEnemies];

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(5);

		gameOverFont = new BitmapFont();
		gameOverFont.setColor(Color.WHITE);
		gameOverFont.getData().setScale(5);



		for(int i =0; i<numberOfEnemies; i++){

			enemyOffSet[i] = (random.nextFloat()-0.5f) * (Gdx.graphics.getHeight()-200);
			enemyOffSet2[i] = (random.nextFloat()-0.5f) * (Gdx.graphics.getHeight()-200);
			enemyOffSet3[i] = (random.nextFloat()-0.5f) * (Gdx.graphics.getHeight()-200);

			enemyX[i] = Gdx.graphics.getWidth()- bee1.getWidth()/2 + i*distance;

			enemyCircles[i] = new Circle();
			enemyCircles2[i] = new Circle();
			enemyCircles3[i] = new Circle();
		}

	}

	@Override
	public void render () {
		if(gameStarted == 0){
			//mainMenuScreen.render(Gdx.graphics.getDeltaTime());
		}
		batch.begin();

		//ArkaPlan resmini konumladırdık
		batch.draw(background,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		if(gameStarted == 1){


			if(enemyX[scoreEnemy] < Gdx.graphics.getWidth()/4){
				score ++;
				if(scoreEnemy < numberOfEnemies -1){
					scoreEnemy ++;
					enemyVelocity += 0.4f;
				}
				else {
					scoreEnemy =0;
				}
			}

			// kuşun uçmasını sağlayan döngü
			if(Gdx.input.justTouched()){
				velocity = -15;
			}

			for(int i =0; i<numberOfEnemies; i++){

				//arıların sürekli dönmesini sağlıyoruz bu kodla
				if(enemyX[i] < birdWidth){
					enemyX[i] = enemyX[i] + numberOfEnemies * distance;
					enemyOffSet[i] = (random.nextFloat()-0.5f) * (Gdx.graphics.getHeight()-200);
					enemyOffSet2[i] = (random.nextFloat()-0.5f) * (Gdx.graphics.getHeight()-200);
					enemyOffSet3[i] = (random.nextFloat()-0.5f) * (Gdx.graphics.getHeight()-200);
				}
				else{
					enemyX[i] = enemyX[i] -enemyVelocity;
				}


				batch.draw(bee1, enemyX[i],Gdx.graphics.getHeight()/2 + enemyOffSet[i],birdWidth,birdHeight);
				batch.draw(bee2, enemyX[i],Gdx.graphics.getHeight()/2 + enemyOffSet2[i],birdWidth,birdHeight);
				batch.draw(bee3, enemyX[i],Gdx.graphics.getHeight()/2 + enemyOffSet3[i],birdWidth,birdHeight);

				enemyCircles[i] = new Circle(enemyX[i]+birdWidth/2 ,Gdx.graphics.getHeight()/2 + enemyOffSet[i] +birdHeight/3,birdWidth/3);
				enemyCircles2[i] = new Circle(enemyX[i]+birdWidth/2 ,Gdx.graphics.getHeight()/2 + enemyOffSet2[i] +birdHeight/3,birdWidth/3);
				enemyCircles3[i] = new Circle(enemyX[i]+birdWidth/2 ,Gdx.graphics.getHeight()/2 + enemyOffSet3[i] +birdHeight/3,birdWidth/3);


			}



			// kuşun ekran altına inmemesi için yaptık
			if(birdY>0 ){
				velocity = velocity+gravity;
				birdY = birdY - velocity;
			}
			else {
				gameStarted = 2;
			}
		}
		else if(gameStarted ==0){
			if(Gdx.input.justTouched()){
				gameStarted = 1;
			}
		} else if(gameStarted == 2){

			gameOverFont.draw(batch,"Oyun Bitti ! Tekrar Oynamak için Dokun",Gdx.graphics.getWidth()/4,Gdx.graphics.getHeight()/2);
			if(Gdx.input.justTouched()){

				gameStarted =1;

				birdY =Gdx.graphics.getHeight()/2;

				for(int i =0; i<numberOfEnemies; i++){

					enemyOffSet[i] = (random.nextFloat()-0.5f) * (Gdx.graphics.getHeight()-200);
					enemyOffSet2[i] = (random.nextFloat()-0.5f) * (Gdx.graphics.getHeight()-200);
					enemyOffSet3[i] = (random.nextFloat()-0.5f) * (Gdx.graphics.getHeight()-200);

					enemyX[i] = Gdx.graphics.getWidth()- bee1.getWidth()/2 + i*distance;

					enemyCircles[i] = new Circle();
					enemyCircles2[i] = new Circle();
					enemyCircles3[i] = new Circle();
				}
				velocity =0;
				scoreEnemy =0;
				score = 0;
				enemyVelocity = 10;
			}
		}

		//kuşu konumlandırdık
		batch.draw(bird,birdX,birdY,birdWidth, birdHeight);

		font.draw(batch,String.valueOf("Score :"+score),100,200);

		batch.end();
		birdCircle.set(birdX+birdWidth/2,birdY+birdHeight/3,birdWidth/3);

		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);


		for(int i =0;i<numberOfEnemies; i++){
				shapeRenderer.circle(enemyX[i]+birdWidth/2 ,Gdx.graphics.getHeight()/2 + enemyOffSet[i] +birdHeight/2,birdWidth/2);
				shapeRenderer.circle(enemyX[i]+birdWidth/2 ,Gdx.graphics.getHeight()/2 + enemyOffSet2[i] +birdHeight/2,birdWidth/2);
				shapeRenderer.circle(enemyX[i]+birdWidth/2 ,Gdx.graphics.getHeight()/2 + enemyOffSet3[i] +birdHeight/2,birdWidth/2);

			if(Intersector.overlaps(birdCircle, enemyCircles[i]) || Intersector.overlaps(birdCircle, enemyCircles2[i]) || Intersector.overlaps(birdCircle, enemyCircles3[i])){
				System.out.println("collision detected");
				gameStarted =2;
			}
		}
		shapeRenderer.end();
	}

	@Override
	public void dispose () {

	}
}
