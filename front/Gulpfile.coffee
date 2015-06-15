gulp = require 'gulp'
sourcemaps = require 'gulp-sourcemaps'
concat = require 'gulp-concat'
babel = require 'gulp-babel'
sass = require 'gulp-sass'
jade = require 'gulp-jade'
templateCache = require 'gulp-angular-templatecache'
merge = require 'merge2'
watch = require 'gulp-watch'
source = require 'vinyl-source-stream'
browserify = require 'browserify'
babelify = require 'babelify'

dist = (path) ->
  '../public/' + path
bower = (path) ->
  './bower_components/' + path

gulp.task 'js', () ->
  browserify
      entries: ['./src/module.js']
      extensions: ['.js']
    .transform(babelify)
    .bundle()
    .pipe source('main.js')
    .pipe gulp.dest(dist('javascripts')),

gulp.task 'jade', () ->
  gulp.src './src/**/*.jade'
    .pipe jade({ doctype: 'html' })
    .pipe templateCache({ module: 'm3hack-d' })
    .pipe gulp.dest(dist('javascripts'))

gulp.task 'sass', () ->
  gulp.src './src/**/*.scss'
    .pipe sass()
    .pipe sourcemaps.write()
    .pipe concat('main.css')
    .pipe gulp.dest(dist('stylesheets'))

gulp.task 'vendor', () ->
  gulp.src [
    bower('flat-ui/dist/css/vendor/bootstrap.min.css'),
    bower('flat-ui/dist/css/flat-ui.css'),
    bower('angular-bootstrap/ui-bootstrap-csp.css')
  ]
    .pipe concat('vendor.css')
    .pipe gulp.dest(dist('stylesheets'))

  gulp.src [bower('flat-ui/dist/css/**/*.map')]
    .pipe gulp.dest(dist('stylesheets'))

  gulp.src [bower('flat-ui/dist/fonts/**/*')]
    .pipe gulp.dest(dist('fonts'))

  gulp.src [
    bower('jquery/dist/jquery.min.js'),
    bower('flat-ui/dist/js/flat-ui.min.js'),
    bower('angular/angular.js'),
    bower('angular-resource/angular-resource.min.js'),
    bower('angular-animate/angular-animate.min.js'),
    bower('angular-bootstrap/ui-bootstrap.min.js'),
    bower('angular-bootstrap/ui-bootstrap-tpls.min.js'),
    bower('angular-ui-router/release/angular-ui-router.min.js'),
  ]
    .pipe concat('vendor.js')
    .pipe gulp.dest(dist('javascripts'))

  gulp.src [
    bower('jquery/dist/jquery.min.map'),
    bower('angular/angular.min.js.map'),
    bower('angular-resource/angular-resource.min.js.map'),
    bower('angular-animate/angular-animate.min.js.map'),
  ]
    .pipe gulp.dest(dist('javascripts'))


gulp.task 'compile', ['js', 'jade', 'sass', 'vendor']
gulp.task 'watch', ['compile'], () ->
  gulp.watch('./src/**/*.scss', ['sass'])
  gulp.watch('./src/**/*.js', ['js'])
  gulp.watch('./src/**/*.jade', ['jade'])
gulp.task 'default', ['watch']
