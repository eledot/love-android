package net.schattenkind.androidLove.luan;

import net.schattenkind.androidLove.LoveVM;
import net.schattenkind.androidLove.Vector3;

import org.luaj.vm2.LuaNumber;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

import android.util.Log;

public class LuanAudio extends LuanBase {
	public static final String SOURCE_TYPE_STATIC = "static";
	public static final String SOURCE_TYPE_STREAM = "stream";

	private Vector3 orientationUp = new Vector3(0.0f, 0.0f, 1.0f);
	private Vector3 orientationForward = new Vector3(1.0f, 0.0f, 0.0f);
	private Vector3 position = new Vector3();
	private Vector3 velocity = new Vector3();

	static final String sMetaName_LuanSource = "__MetaLuanSource";
	static final String sMetaName_LuanDecoder = "__MetaLuanDecoder";
	static final String sMetaName_LuanSoundData = "__MetaLuanSoundData";
	
	public static void Log (String s) { Log.i("LuanAudio", s); }
	
	// 0.0f - 1.0f
	private float volume = 1.0f;

	public LuanAudio(LoveVM vm) {
		super(vm);
	}

	// call this if position, velocity, ... changed
	public void notifySpatialChange() {
		// TODO
	}

	public LuaTable InitLib() {
		LuaTable t = LuaValue.tableOf();
		
		LuaValue _G = vm.get_G();
		
		_G.set(sMetaName_LuanSource,LuanSource.CreateMetaTable());
		_G.set(sMetaName_LuanDecoder,LuanDecoder.CreateMetaTable());
		_G.set(sMetaName_LuanSoundData,LuanSoundData.CreateMetaTable());
		

		// numSources = love.audio.getNumSources( )
		t.set("getNumSources", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return LuaValue.ZERO;
			}
		});

		// fx, fy, fz, ux, uy, uz = love.audio.getOrientation( )
		t.set("getOrientation", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return varargsOf(new LuaValue[] {
						LuaNumber.valueOf(orientationForward.x),
						LuaNumber.valueOf(orientationForward.y),
						LuaNumber.valueOf(orientationForward.z),
						LuaNumber.valueOf(orientationUp.x),
						LuaNumber.valueOf(orientationUp.y),
						LuaNumber.valueOf(orientationUp.z) });
			}
		});

		// x, y, z = love.audio.getPosition( )
		t.set("getPosition", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return varargsOf(new LuaValue[] {
						LuaNumber.valueOf(position.x),
						LuaNumber.valueOf(position.y),
						LuaNumber.valueOf(position.z) });
			}
		});

		// x, y, z = love.audio.getVelocity( )
		t.set("getVelocity", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return varargsOf(new LuaValue[] {
						LuaNumber.valueOf(velocity.x),
						LuaNumber.valueOf(velocity.y),
						LuaNumber.valueOf(velocity.z) });
			}
		});

		// volume = love.audio.getVolume( )
		t.set("getVolume", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return varargsOf(new LuaValue[] { LuaNumber.valueOf(volume) });
			}
		});

		// source = love.audio.newSource( file, type )
		t.set("newSource", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				if (args.isstring(1)) {
					String sFileName = args.checkjstring(1);
					String sType = args.checkjstring(2);
					return LuaValue.userdataOf(new LuanSource(LuanAudio.this,sFileName,sType),vm.get_G().get(sMetaName_LuanSource));
				}
				if (args.narg() >= 2 && args.isstring(2)) {
					LuanDecoder decoder = (LuanDecoder)args.checkuserdata(1,LuanDecoder.class);
					String sType = args.checkjstring(2);
					return LuaValue.userdataOf(new LuanSource(LuanAudio.this,decoder,sType),vm.get_G().get(sMetaName_LuanSource));
				}
				LuanSoundData soundata = (LuanSoundData)args.checkuserdata(1,LuanSoundData.class);
				return LuaValue.userdataOf(new LuanSource(LuanAudio.this,soundata),vm.get_G().get(sMetaName_LuanSource));
			}
		});

		// love.audio.pause( )
		t.set("pause", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				// TODO
				return LuaValue.NONE;
			}
		});

		// love.audio.play( source )
		t.set("play", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				// TODO
				return LuaValue.NONE;
			}
		});

		// love.audio.resume( )
		t.set("resume", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				// TODO
				return LuaValue.NONE;
			}
		});

		// love.audio.rewind( )
		t.set("rewind", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				// TODO
				return LuaValue.NONE;
			}
		});

		// love.audio.setOrientation( fx, fy, fz, ux, uy, uz )
		t.set("setOrientation", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				orientationForward.x = args.arg(1).tofloat();
				orientationForward.y = args.arg(2).tofloat();
				orientationForward.z = args.arg(3).tofloat();

				orientationUp.x = args.arg(4).tofloat();
				orientationUp.y = args.arg(5).tofloat();
				orientationUp.z = args.arg(6).tofloat();

				notifySpatialChange();

				return LuaValue.NONE;
			}
		});

		// love.audio.setPosition( x, y, z )
		t.set("setPosition", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				position.x = args.arg(1).tofloat();
				position.y = args.arg(2).tofloat();
				position.z = args.arg(3).tofloat();

				notifySpatialChange();

				return LuaValue.NONE;
			}
		});

		// love.audio.setVelocity( x, y, z )
		t.set("setVelocity", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				position.x = args.arg(1).tofloat();
				position.y = args.arg(2).tofloat();
				position.z = args.arg(3).tofloat();

				notifySpatialChange();

				return LuaValue.NONE;
			}
		});

		// love.audio.setVolume( volume )
		t.set("setVolume", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				volume = args.arg(1).tofloat();

				// TODO

				return LuaValue.NONE;
			}
		});

		// love.audio.stop( )
		t.set("stop", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				// TODO
				return LuaValue.NONE;
			}
		});

		return t;
	}

	
	// ***** ***** ***** ***** *****  LuanSoundData
	
	public static class LuanSoundData {
		public static LuaTable CreateMetaTable () {
			LuaTable mt = LuaValue.tableOf();
			LuaTable t = LuaValue.tableOf();
			mt.set("__index",t);
			return mt;
		}
	}
		
	// ***** ***** ***** ***** *****  LuanDecoder
		
	public static class LuanDecoder {
		public static LuaTable CreateMetaTable () {
			LuaTable mt = LuaValue.tableOf();
			LuaTable t = LuaValue.tableOf();
			mt.set("__index",t);
			return mt;
		}
	}
		
	// ***** ***** ***** ***** *****  LuanSource
	
	public static class LuanSource {
		private LuanAudio	audio;
		
		public LuanSource (LuanAudio audio,String filename,String type) { this.audio = audio; }
		
		public LuanSource (LuanAudio audio,LuanDecoder decoder,String type) { this.audio = audio; }
		
		public LuanSource (LuanAudio audio,LuanSoundData data) { this.audio = audio; }
			
		public static LuaTable CreateMetaTable () {
			LuaTable mt = LuaValue.tableOf();
			LuaTable t = LuaValue.tableOf();
			mt.set("__index",t);
			
			t.set("getDirection",	new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } });	
			t.set("getPitch",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } });	
			t.set("getPosition",	new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } });	
			t.set("getVelocity",	new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } });	
			t.set("getVolume",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } });	
			t.set("isLooping",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } });	
			t.set("isPaused",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } });		
			t.set("isStatic",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } });		
			t.set("isStopped",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } });	
			t.set("pause",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } });	
			t.set("play",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } });		
			t.set("resume",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } });	
			t.set("rewind",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } });	
			t.set("setDirection",	new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } });		
			t.set("setLooping",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } });	
			t.set("setPitch",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } });		
			t.set("setPosition",	new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } });		
			t.set("setVelocity",	new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } });		
			t.set("setVolume",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } });	
			t.set("stop",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } });		

			/// type = Object:type()  , e.g. "Image" or audio:"Source"
			t.set("type", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.valueOf("Source"); } });
			
			/// b = Object:typeOf( name )
			t.set("typeOf", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { 
				String s = args.checkjstring(2); 
				return LuaValue.valueOf(s == "Object" || s == "Source"); 
			} });
			
			
			return mt;
		}
	}
	
}