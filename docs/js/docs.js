/* TuoLingAt 文档站脚本：代码复制 + 目录滚动高亮 */

(function () {
    'use strict';

    // ============ 代码块复制按钮 ============
    function initCopyButtons() {
        var blocks = document.querySelectorAll('.code-block');
        Array.prototype.forEach.call(blocks, function (block) {
            var pre = block.querySelector('pre');
            if (!pre) return;

            var btn = document.createElement('button');
            btn.className = 'copy-btn';
            btn.type = 'button';
            btn.textContent = '复制';

            btn.addEventListener('click', function () {
                copyText(pre.innerText).then(function () {
                    btn.textContent = '已复制';
                    btn.classList.add('done');
                    setTimeout(function () {
                        btn.textContent = '复制';
                        btn.classList.remove('done');
                    }, 1400);
                });
            });

            block.appendChild(btn);
        });
    }

    function copyText(text) {
        if (navigator.clipboard && navigator.clipboard.writeText) {
            return navigator.clipboard.writeText(text).catch(function () {
                fallbackCopy(text);
            });
        }
        fallbackCopy(text);
        return Promise.resolve();
    }

    function fallbackCopy(text) {
        var ta = document.createElement('textarea');
        ta.value = text;
        ta.style.position = 'fixed';
        ta.style.opacity = '0';
        document.body.appendChild(ta);
        ta.select();
        try {
            document.execCommand('copy');
        } catch (e) {
            /* 浏览器不支持时静默失败 */
        }
        document.body.removeChild(ta);
    }

    // ============ 目录滚动高亮 ============
    function initScrollSpy() {
        var items = Array.prototype.slice.call(document.querySelectorAll('.toc-item[href^="#"]'));
        if (!items.length) return;

        var targets = [];
        items.forEach(function (item) {
            var el = document.getElementById(item.getAttribute('href').slice(1));
            if (el) targets.push({ item: item, el: el });
        });
        if (!targets.length) return;

        function highlight() {
            var current = targets[0];
            targets.forEach(function (t) {
                if (t.el.getBoundingClientRect().top <= 90) {
                    current = t;
                }
            });
            items.forEach(function (i) { i.classList.remove('active'); });
            if (current) current.item.classList.add('active');
        }

        var ticking = false;
        window.addEventListener('scroll', function () {
            if (ticking) return;
            ticking = true;
            window.requestAnimationFrame(function () {
                highlight();
                ticking = false;
            });
        }, { passive: true });

        highlight();
    }

    document.addEventListener('DOMContentLoaded', function () {
        initCopyButtons();
        initScrollSpy();
    });
})();
