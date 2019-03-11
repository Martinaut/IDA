const {src, dest, parallel} = require("gulp");
const plumber = require("gulp-plumber");
const notify = require("gulp-notify");
const babel = require("gulp-babel");
const merge = require("merge-stream");

function buildJs() {
    return src("./src/**/*.js")
        .pipe(plumber({
            errorHandler: function (err) {
                notify.onError({
                    title: "Gulp error in " + err.plugin,
                    message: err.toString()
                })(err);
            }
        }))
        .pipe(babel({
            presets: [
                [
                    "@babel/preset-env",
                    {
                        targets: {
                            browsers: ["> 1%", "last 2 versions", "not ie <= 9"]
                        }
                    }
                ]
            ]
        }))
        .pipe(dest("./js"));
}

function copyNpmFiles() {
    const adminCss = src("./node_modules/startbootstrap-sb-admin-2/css/sb-admin-2.min.css").pipe(dest("./css/"));
    const adminJs = src("./node_modules/startbootstrap-sb-admin-2/js/sb-admin-2.min.js").pipe(dest("./js/"));

    const booststrapCss = src("./node_modules/bootstrap/dist/css/bootstrap.min.css*").pipe(dest("./vendor/bootstrap/"));
    const booststrapJs = src([
        "./node_modules/bootstrap/dist/js/bootstrap.min.js*",
        "./node_modules/popper.js/dist/umd/popper.min.js*"]).pipe(dest("./vendor/bootstrap/"));

    const jquery = src([
        "./node_modules/jquery/dist/jquery.min.js",
        "./node_modules/jquery.easing/jquery.easing.min.js"]).pipe(dest("./vendor/jquery/"));

    const sockjs = src("./node_modules/sockjs-client/dist/sockjs.min.js*").pipe(dest("./vendor/sockjs/"));
    const stomp = src("./node_modules/stompjs/lib/stomp.min.js").pipe(dest("./vendor/stomp/"));

    return merge(adminCss, adminJs, booststrapCss, booststrapJs, jquery, sockjs, stomp);
}

exports.js = buildJs;
exports.default = parallel(buildJs, copyNpmFiles);