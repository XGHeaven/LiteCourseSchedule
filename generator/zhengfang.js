(function(){
function Course(name, classroom, start, last, day) {
    this.name = name;
    this.classroom = classroom;
    this.start = start;
    this.last = last;
    this.day = day;
}

var
    coursePattern = /^(.*?)\n周([一二三四五六日])第([\d]+),([\d]+)节\{第([\d]+)-([\d]+)周\}\n(.*?)\n(.*?)$/,
    courseAddPattern = /^(.*?)\n周([一二三四五六日])第([\d]+)节\{第([\d]+)-([\d]+)周\}\n(.*?)\n(.*?)$/,
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
        name, classroom, start, last, day;

    if (match != null) {
        name = match[1];
        classroom = match[8];
        start = parseInt(match[3]);
        last = parseInt(match[4]) - start + 1;
        day = weeks.indexOf(match[2]);
        courses.push(new Course(name, classroom, start, last, day));
        continue;
    }

    match = td.innerText.match(courseAddPattern);

    if (match != null) {
        name = match[1];
        start = parseInt(match[3]);
        for (var j=0; j<courses.length; j++) {
            if (courses[j].name == name) {
                courses[j].last = start - courses[j].start + 1;
                break;
            }
        }
    }
}

window.open(url + encodeURIComponent(JSON.stringify(courses)));
})()
