var args = [ './test2.js', /* command arguments */ ];
var child = spawn(process.execPath, args, { stdio: ['pipe', 1, 2, 'ipc'] });
file.pipe(child.stdin);
