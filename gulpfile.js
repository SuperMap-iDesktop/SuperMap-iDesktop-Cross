'use strict';

var gulp = require('gulp');
var $ = require('gulp-load-plugins')();
var cssnano = require('cssnano');
var uglify = require('gulp-uglify');
var minifycss = require('gulp-minify-css');
var imagemin = require('gulp-imagemin');
var htmlmin = require('gulp-htmlmin');

/*var dirs = {
  public: 'public',
  screenshots: 'public/build/screenshots'
};

gulp.task('useref', ['screenshot'], function(){
  var assets = $.useref.assets({
    searchPath: 'public'
  });*/

  //return gulp.src('public/**/*.html')
    /*.pipe(assets)
    .pipe($.uniqueFiles())
    .pipe($.if('*.css', $.postcss([
      cssnano()
    ])))
    .pipe($.if('*.js', $.uglify()))
    .pipe($.rev())
    .pipe(assets.restore())
    .pipe($.useref())
    .pipe($.revReplace({
      prefix: '/'
    }))
    .pipe(gulp.dest('public'));
});*/

// 压缩 public/js 目录 js
gulp.task('minify-js', function() {
    return gulp.src('public/**/*.js')
        .pipe(uglify())
        .pipe(gulp.dest('public'));
}).on('task_start',function(){
    console.log('start');
}).on('task_err',function(err){
    console.log('error');
}).on('task_stop',function(){
    console.log('stop');
});
// 压缩 public 目录 css
gulp.task('minify-css', function() {
    return gulp.src('public/**/*.css')
        .pipe(minifycss())
        .pipe(gulp.dest('public'));
});
// 压缩图片任务
// 在命令行输入 gulp images 启动此任务
gulp.task('images', function () {
    // 1. 找到图片
    gulp.src('public/**/*.png')
        // 2. 压缩图片
        .pipe(imagemin({
            progressive: true
        }))
        // 3. 另存图片
        .pipe(gulp.dest('public'))
});

// 执行 gulp 命令时执行的任务
gulp.task('default', ['minify-css','minify-js','images']);