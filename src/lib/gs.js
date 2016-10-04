var exec = require('child_process').exec;
var spawn = require('child_process').spawn;
var gsExecutableName = ( /^win/.test(process.platform) ? 'gswin64c' : 'gs')


var create = function() {
  return new gs();
};

var gs = function() {
  this.options = [];
  this._input = null;
};

gs.prototype.batch = function() {
  this.options.push('-dBATCH');
  return this;
};

gs.prototype.device = function(device) {
  device = device || 'jpeg';
  this.options.push('-sDEVICE=' + device);
  return this;
};

gs.prototype.exec = function(callback) {
  var self = this;

  if (!this._input) return callback("Please specify input");

  var args = this.options.concat([this._input]).join(' ');
  // console.log('#' + this.options.constructor.name)
  exec(gsExecutableName + ' ' + args, function(err, stdout, stderr) {
    callback(err, stdout, stderr);
  });
};

gs.prototype.spawn = function(callback) {
  var self = this;

  if (!this._input) return callback("Please specify input");

  var args = this.options.concat([this._input]).join(' ');
  // var cmd = spawn('tee', /*this.options*/ ['test.txt']);
  this.options.push('-')
  var cmd = spawn(gsExecutableName, this.options);
  var Readable = require('stream').Readable

  var s = new Readable
  s.push(this._input) // the string you want
  s.push(null)
  s.pipe(cmd.stdin)
  cmd.on('close', (code) => {
    // callback(err, stdout, stderr);
  });

};

gs.prototype.input = function(file) {
  this._input = file;
  return this;
};

gs.prototype.jpegq = function(value) {
  value = value || 75;
  this.options.push('-dJPEGQ=' + value);
  return this;
};

gs.prototype.nopause = function() {
  this.options.push('-dNOPAUSE');
  return this;
};

gs.prototype.output = function(file) {
  file = file || '-';
  this.options.push('-sOutputFile=' + file);
  if (file === '-') return this.quiet();
  return this;
};

gs.prototype.q = gs.prototype.quiet;

gs.prototype.quiet = function() {
  this.options.push('-dQUIET');
  return this;
};

gs.prototype.resolution = function(xres, yres) {
  this.options.push('-r' + xres + (yres ? 'x' + yres : ''));
  return this;
};

gs.prototype.r = gs.prototype.res = gs.prototype.resolution;

module.exports = create;
