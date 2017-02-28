(function(){
function Course(name, classroom, start, last, day) {
    this.name = name;
    this.classroom = classroom;
    this.start = start;
    this.last = last;
    this.day = day;
}

Course.courses = [];

Course.union = function(c) {
    for (var i=0; i<this.courses.length; i++) {
        var course = this.courses[i];
        if (course.name == c.name && course.start + course.last == c.start) {
            course.last += c.last;
            return;
        }
    }
    this.courses.push(c);
}

var
    coursePattern = /^(.*?)\n周([一二三四五六日])第((?:[\d]+)(?:,[\d]+)*)节\{第([\d]+)-([\d]+)周(?:\|(.*?))?\}\n(.*?)\n(.*?)$/,
    weeks = ['一', '二', '三', '四', '五', '六', '日'],
    url = 'http://pan.baidu.com/share/qrcode?w=500&h=500&url=',
    win = document.getElementById("iframeautoheight").contentWindow,
    doc = win.document,
    tds = doc.getElementById("Table1").getElementsByTagName("td"),
    courses = [];

for (var i=0; i<tds.length; i++) {
    var
        td = tds[i],
        match = td.innerText.match(coursePattern),
        name, classroom, start, last, day, times;

    if (match != null) {
        name = match[1];
        classroom = match[8];
        times = match[3].split(',');
        start = parseInt(times[0]);
        last = parseInt(times[times.length - 1]) - start + 1;
        day = weeks.indexOf(match[2]);
        Course.union(new Course(name, classroom, start, last, day));
        continue;
    }

}

window.open(url + encodeURIComponent(JSON.stringify(Course.courses)));
})()
