package net.schattenkind.androidLove.luan.module;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import net.schattenkind.androidLove.LoveStorage;
import net.schattenkind.androidLove.LoveVM;
import net.schattenkind.androidLove.luan.LuanBase;
import net.schattenkind.androidLove.utils.LuaUtils;

import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaBoolean;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;



public class LuanFilesystem extends LuanBase {

	protected static final String TAG = "LoveFilesystem";

	public LuanFilesystem(LoveVM vm) {
		super(vm);
	}
	
	public void Log (String s) { LoveVM.LoveLog(TAG, s); }

	public LuaTable InitLib() {
		LuaTable t = LuaValue.tableOf();

		// files = love.filesystem.enumerate( dir )
		t.set("enumerate", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				try {
					String path = args.arg1().toString();
					//~ Log("enumerate:"+path);
					String[] children = vm.getStorage().getChildren(path);
					//~ if (children == null) Log("enumerate=error");
					if (children == null) return LuaValue.NONE;
					LuaTable t = LuaValue.tableOf();
					//~ for (int i=0;i<children.length;++i) Log("enumerate[]="+children[i]);
					for (int i=0;i<children.length;++i) t.rawset(i+1,children[i]);
					return t;
				} catch (IOException e) {
					vm.handleError(e);
					return LuaValue.NONE;
				}
			}
		});

		// e = love.filesystem.exists( filename )
		t.set("exists", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return LuaBoolean.valueOf(vm.getStorage().getFileType(
						args.arg1().toString()) != LoveStorage.FileType.NONE);
			}
		});

		// path = love.filesystem.getAppdataDirectory( )
		t.set("getAppdataDirectory", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return LuaString.valueOf(vm.getStorage().getRootPath());
			}
		});

		// modtime, errormsg = love.filesystem.getLastModified( filename )
		t.set("getLastModified", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				// TODO
				vm.NotImplemented("love.filesystem.getLastModified");
				return LuaValue.varargsOf(new LuaValue[] { LuaValue.NIL,
						LuaValue.NIL });
			}
		});

		// dir = love.filesystem.getSaveDirectory( )
		t.set("getSaveDirectory", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return LuaString.valueOf(vm.getStorage().getRootPath());
			}
		});

		// path = love.filesystem.getUserDirectory( )
		t.set("getUserDirectory", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return LuaString.valueOf(vm.getStorage().getRootPath());
			}
		});

		// cwd = love.filesystem.getWorkingDirectory( )
		t.set("getWorkingDirectory", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return LuaString.valueOf(vm.getStorage().getRootPath());
			}
		});

		// love.filesystem.init( )
		t.set("init", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return LuaValue.NONE;
			}
		});

		// is_dir = love.filesystem.isDirectory( filename )
		t.set("isDirectory", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return LuaBoolean.valueOf(vm.getStorage().getFileType(
						args.arg1().toString()) == LoveStorage.FileType.DIR);
			}
		});

		// is_file = love.filesystem.isFile( filename )
		t.set("isFile", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return LuaBoolean.valueOf(vm.getStorage().getFileType(
						args.arg1().toString()) == LoveStorage.FileType.FILE);
			}
		});

		// iterator = love.filesystem.lines( name )
		// ---------------------------
		// local highscores = {}
		// for line in love.filesystem.lines("highscores.lst") do
		// table.insert(highscores, line)
		// end
		t.set("lines", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				String filename = args.arg1().toString();

				try {
					List<String> lines;
					lines = vm.getStorage().getLines(filename);
					LuaTable t = LuaTable.tableOf(LuaUtils
							.convertStringListToLuaStringArray(lines));
					return vm.get_G().get("love_andorid_list_iter").call(t);
				} catch (IOException e) {
					vm.handleError(e);
					return LuaValue.NONE;
				}

			}
		});

		// chunk = love.filesystem.load( name )
		t.set("load", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				String filename = args.arg1().toString();
				try {
					LuaFunction f = LoadState.load(vm.getStorage()
							.getFileStreamFromLovePath(filename), filename, vm
							.get_G());
					return f;
				} catch (FileNotFoundException e) {
					LoveVM.LoveLogE(TAG, e.getMessage());
					return LuaValue.NONE;
				} catch (IOException e) {
					LoveVM.LoveLogE(TAG, e.getMessage());
					return LuaValue.NONE;
				}
			}
		});

		// ok = love.filesystem.mkdir( name )
		t.set("mkdir", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				// TODO
				vm.NotImplemented("love.filesystem.mkdir");
				return LuaValue.FALSE;
			}
		});

		// file = love.filesystem.newFile( filename )
		t.set("newFile", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				// TODO
				vm.NotImplemented("love.filesystem.newFile");
				return LuaValue.NIL;
			}
		});

		// data = love.filesystem.newFileData( contents, name, decoder )
		t.set("newFileData", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				// TODO
				vm.NotImplemented("love.filesystem.newFileData");
				return LuaValue.NIL;
			}
		});

		// contents, size = love.filesystem.read( name, size )
		t.set("read", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				// TODO
				vm.NotImplemented("love.filesystem.read");
				return LuaValue.NONE;
			}
		});

		// ok = love.filesystem.remove( name )
		t.set("remove", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				// TODO
				vm.NotImplemented("love.filesystem.remove");
				return LuaValue.FALSE;
			}
		});

		// love.filesystem.setIdentity( name )
		t.set("setIdentity", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				// TODO
				vm.NotImplemented("love.filesystem.setIdentity");
				return LuaValue.NONE;
			}
		});

		// love.filesystem.setSource( )
		t.set("setSource", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				// TODO
				vm.NotImplemented("love.filesystem.setSource");
				return LuaValue.NONE;
			}
		});

		// success = love.filesystem.write( name, data, size )
		t.set("write", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				// TODO
				vm.NotImplemented("love.filesystem.write");
				return LuaValue.FALSE;
			}
		});

		return t;
	}

}
