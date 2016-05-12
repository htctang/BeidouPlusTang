package com.example.beidouplus.myview;

import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Sphere;
import org.rajawali3d.renderer.Renderer;

import com.example.beidouplus.R;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by clintonmedbery on 4/6/15.
 */
public class MyTestRenderer extends Renderer {

 
	/**
	 * @param context
	 * @param pipScale
	 * @param pipMarginX
	 * @param pipMarginY
	 */
	

	public Context context;

    private DirectionalLight directionalLight;
    private Sphere earthSphere;

   
 	public MyTestRenderer(Context context) {
 		super(context);
 		 this.context = context;
	        setFrameRate(60);
 	}

    
 

    public void initScene(){

        directionalLight = new DirectionalLight(1f, .2f, -1.0f);
        directionalLight.setColor(1.0f, 1.0f, 1.0f);
        directionalLight.setPower(2);
        getCurrentScene().addLight(directionalLight);

        Material material = new Material();
        material.enableLighting(true);
        material.setDiffuseMethod(new DiffuseMethod.Lambert());
        material.setColor(Color.BLACK);
        

        Texture earthTexture = new Texture("Earth", R.drawable.earthtruecolor_nasa_big);
        try{
            material.addTexture(earthTexture);

        } catch (ATexture.TextureException error){
            Log.d("DEBUG", "TEXTURE ERROR");
        }
        //这里的sphere后面的两个参数 控制三角形的数量 如果数值越小就证明其越不像圆 这个半径的问题还有待考察
        earthSphere = new Sphere(1, 12, 12);
        
        earthSphere.setMaterial(material);
        getCurrentScene().addChild(earthSphere);
        getCurrentCamera().setZ(4.2f);

    }


    @Override
     public void onRender(final long elapsedTime, final double deltaTime) {
        super.onRender(elapsedTime, deltaTime);
        earthSphere.rotate(Vector3.Axis.Y, 1.0);
    }


    public void onTouchEvent(MotionEvent event){


    }

    public void onOffsetsChanged(float x, float y, float z, float w, int i, int j){

    }
}

