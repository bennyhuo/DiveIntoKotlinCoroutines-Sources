import * as https from "https";

const urls = [
    "https://www.bennyhuo.com/assets/avatar.jpg",
    "https://www.bennyhuo.com/assets/avatar.jpg",
    "https://www.bennyhuo.com/assets/avatar.jpg"
];

function main() {
    Promise.all(urls.map(url => bitmapPromise(url)))
        .then(bitmaps => console.log(bitmaps.length))
        .catch(e => console.error(e));
}

async function asyncMain() {
    try {
        const bitmaps = await Promise.all(urls.map(url => bitmapPromise(url)));
        console.log(bitmaps);
    } catch (e) {
        console.error(e);
    }
}

function bitmapPromise(url) {
    return new Promise((resolve, reject) => {
        try {
            download(url, resolve)
        } catch (e) {
            reject(e)
        }
    })
}

function download(url, callback) {
    https.get(url, (res) => {
            let data = '';
            res.on('data', chunk => {
                data += chunk;
            });
            res.on('end', () => {
                callback(data)
            });
        }
    );
}

// main();
asyncMain();